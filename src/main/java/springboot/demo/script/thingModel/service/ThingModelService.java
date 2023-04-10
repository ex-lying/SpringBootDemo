package springboot.demo.script.thingModel.service;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springboot.demo.excel.model.ThingModelExcel;
import springboot.demo.kafka.KafkaSender;
import springboot.demo.mysql.mapper.*;
import springboot.demo.mysql.model.ThingModel;
import springboot.demo.mysql.model.ThingModelProduct;
import springboot.demo.mysql.model.ThingModelProductDetail;
import springboot.demo.mysql.vo.IotPropertyTranslateBo;
import springboot.demo.mysql.vo.ThingModelSpecs;
import springboot.demo.mysql.vo.specs.*;
import tool.util.JsonUtil;
import tool.util.LogUtil;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @className: ThingModelService
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/24 上午10:43
 */
@Service
public class ThingModelService {
    private static final LogUtil log = new LogUtil(ThingModelService.class);

    //to-iot的数据物模型操作类型
    private static final String OPERATE_DELETE = "delete";
    private static final String OPERATE_ADD = "add";
    private static final String OPERATE_UPDATE = "update";
    //展示分类默认值
    private static final String DEFAULT_SHOW_TYPE = "details";

    //未成功添加列表
    public static Map<ThingModelExcel, String> errorExcel = new HashMap<>();

    //系列品牌主板标识列表
    private static final Map<String, Map<String, List<String>>> serailBrandMainboardMap;

    //数据类型列表
    private static final List<String> typeList = Arrays.asList("int", "double", "enum", "bool", "string", "date", "array");

    //展示分类列表
    private static final List<String> showTypeList = Arrays.asList("GB", "details", "more");

    //是否预警列表
    private static final List<Integer> isEarlyWarningList = Arrays.asList(0, 1);

    static {
        //系列品牌主板标识列表
        serailBrandMainboardMap = new HashMap<>();

        Map<String, List<String>> E100C = new HashMap<>();
        E100C.put("rofound", List.of("rofound"));

        serailBrandMainboardMap.put("E100-C", E100C);

        Map<String, List<String>> E100W = new HashMap<>();
        E100W.put("rofound", List.of("rofound"));

        serailBrandMainboardMap.put("E100-W", E100W);

        Map<String, List<String>> E100M = new HashMap<>();
        E100M.put("rofound", List.of("rofound"));

        serailBrandMainboardMap.put("E100-M", E100M);

        Map<String, List<String>> monake = new HashMap<>();
        monake.put("monake", List.of("monake"));

        serailBrandMainboardMap.put("monake", monake);

        Map<String, List<String>> elevatorstar = new HashMap<>();
        elevatorstar.put("elevatorstar", List.of("elevatorstar"));

        serailBrandMainboardMap.put("elevatorstar", elevatorstar);

        Map<String, List<String>> qiaotong = new HashMap<>();
        qiaotong.put("qiaotong", List.of("qiaotong"));

        serailBrandMainboardMap.put("qiaotong", qiaotong);

        Map<String, List<String>> S110 = new HashMap<>();
        S110.put("rofound", Arrays.asList("SingleDZS", "DoubleDZS", "MicroDoubleDZS", "rofound"));

        serailBrandMainboardMap.put("S110", S110);

        Map<String, List<String>> P110 = new HashMap<>();
        P110.put("Thyssenkrupp", List.of("Mc2"));

        serailBrandMainboardMap.put("P110", P110);

        Map<String, List<String>> P100 = new HashMap<>();
        P100.put("Kone", List.of("Cpu561"));
        P100.put("Hitachi", List.of("Mca09"));
        P100.put("Yungtay", List.of("Mpugb2"));
        P100.put("Mitsubishi", Arrays.asList("Kcd1161c", "Lehy758"));
        P100.put("Otis", Arrays.asList("Gecb", "Hamcb", "Tcbc"));
        P100.put("Schindler", List.of("Cadigc"));
        P100.put("Thyssenkrupp", List.of("Mc2"));

        serailBrandMainboardMap.put("P100", P100);
    }

    @Autowired
    ThingModelMapper thingModelMapper;

    @Autowired
    ThingModelProductMapper thingModelProductMapper;

    @Autowired
    ThingModelProductDetailMapper thingModelProductDetailMapper;

    @Autowired
    KafkaSender kafkaSender;

    @Autowired
    AuthCompanyMapper authCompanyMapper;

    @Autowired
    AuthDeptMapper authDeptMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.kafka.topicName.iotPropertyTranslate}")
    private String iotPropertyTranslate;

    private final static String standarKey = "iot:standard:companyUniqueId:_%s";

    private final static String hashkey = "propertyStandard";

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean insertThingModel(ThingModelExcel thingModelExcel) {
        if (!validateExcel(thingModelExcel)) {
            return false;
        }

        ThingModelSpecs specs = null;

        try {
            specs = validateSpecs(thingModelExcel.getSpecs(), thingModelExcel.getType(), thingModelExcel.getIsEarlyWarning());
        } catch (Exception e) {
            errorExcel.put(thingModelExcel, e.getMessage());

            return false;
        }

        QueryWrapper<ThingModel> thingModelQueryWrapper = new QueryWrapper<>();
        thingModelQueryWrapper.eq("project", thingModelExcel.getProject());

        ThingModel thingModel = thingModelMapper.selectOne(thingModelQueryWrapper);

        if (thingModel == null) {

            thingModel = new ThingModel()
                    .setProject(thingModelExcel.getProject())
                    .setIsDefault(1)
                    .setIsEfficient(1)
                    .setCreateTime(LocalDateTime.now())
                    .setCreateUser("System")
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser("System");

            thingModelMapper.insert(thingModel);
        } else {
            thingModel
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser("System");

            thingModelMapper.updateById(thingModel);
        }

        QueryWrapper<ThingModelProduct> tmpQueryWrapper = new QueryWrapper<>();
        tmpQueryWrapper
                .eq("thing_model_id", thingModel.getId())
                .eq("serial", thingModelExcel.getSerial())
                .eq("brand", thingModelExcel.getBrand())
                .eq("mainboard", thingModelExcel.getMainboard());

        ThingModelProduct thingModelProduct = thingModelProductMapper.selectOne(tmpQueryWrapper);

        if (thingModelProduct == null) {
            thingModelProduct = new ThingModelProduct()
                    .setThingModelId(thingModel.getId())
                    .setSerial(thingModelExcel.getSerial())
                    .setBrand(thingModelExcel.getBrand())
                    .setMainboard(thingModelExcel.getMainboard())
                    .setIsEfficient(1)
                    .setCreateTime(LocalDateTime.now())
                    .setCreateUser("System")
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser("System");

            thingModelProductMapper.insert(thingModelProduct);
        } else {
            thingModelProduct
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser("System");

            thingModelProductMapper.updateById(thingModelProduct);
        }

        QueryWrapper<ThingModelProductDetail> tmpdQueryWrapper = new QueryWrapper<>();
        tmpdQueryWrapper.eq("thing_model_product_id", thingModelProduct.getId())
                .and(wrapper -> {
                    wrapper
                            .eq("identifier", thingModelExcel.getIdentifier())
                            .or()
                            .eq("name", thingModelExcel.getName())
                            .or()
                            .eq("sort", thingModelExcel.getOrder());

                });

        ThingModelProductDetail tmpd = thingModelProductDetailMapper.selectOne(tmpdQueryWrapper);

        if (tmpd != null) {
            log.error("物模型重复添加:" + JsonUtil.toJson(thingModelExcel));

            errorExcel.put(thingModelExcel, "物模型重复添加");
        } else {
            tmpd = new ThingModelProductDetail()
                    .setThingModelProductId(thingModelProduct.getId())
                    .setName(thingModelExcel.getName())
                    .setIdentifier(thingModelExcel.getIdentifier())
                    .setSort(thingModelExcel.getOrder())
                    .setSpecs(JsonUtil.toJson(specs))
                    .setType(thingModelExcel.getType())
                    .setExpectedValue(thingModelExcel.getExpectedValue())
                    .setUnits(thingModelExcel.getUnits())
                    .setIsEarlyWarning(thingModelExcel.getIsEarlyWarning())
                    .setIsEfficient(1)
                    .setCategory(DEFAULT_SHOW_TYPE)
                    .setCreateUser("System")
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateUser("System")
                    .setUpdateTime(LocalDateTime.now());

            thingModelProductDetailMapper.insert(tmpd);

            IotPropertyTranslateBo bo = new IotPropertyTranslateBo()
                    .setProject(thingModel.getProject())
                    .setOriginProject(thingModel.getProject())
                    .setSerial(thingModelProduct.getSerial())
                    .setOriginSerial(thingModelProduct.getSerial())
                    .setBrand(thingModelProduct.getBrand())
                    .setOriginBrand(thingModelProduct.getBrand())
                    .setMainboard(thingModelProduct.getMainboard())
                    .setOriginMainboard(thingModelProduct.getMainboard())
                    .setIdentifier(tmpd.getIdentifier())
                    .setOriginIdentifier(tmpd.getIdentifier())
                    .setName(tmpd.getName())
                    .setSort(tmpd.getSort())
                    .setSpecs(tmpd.getSpecs())
                    .setType(tmpd.getType())
                    .setExpectedValue(tmpd.getExpectedValue())
                    .setUnits(tmpd.getUnits())
                    .setIsEarlyWarning(tmpd.getIsEarlyWarning())
                    .setOperateType(OPERATE_ADD);

            sendIotPropertyTranslate(List.of(bo));
        }

        return true;
    }

    private Boolean validateExcel(ThingModelExcel thingModelExcel) {
        if (thingModelExcel.getProject().length() > 16) {
            ThingModelService.errorExcel.put(thingModelExcel, "物模型名称不能超过16个字符");

            return false;
        }

        if (thingModelExcel.getIdentifier().length() > 24) {
            ThingModelService.errorExcel.put(thingModelExcel, "物模型功能标识符不能超过24个字符");

            return false;
        }

        if (thingModelExcel.getName().length() > 12) {
            ThingModelService.errorExcel.put(thingModelExcel, "物模型功能名称不能超过12个字符");

            return false;
        }

        if (thingModelExcel.getExpectedValue().length() > 24) {
            ThingModelService.errorExcel.put(thingModelExcel, "物模型功能期望值不能超过24个字符");

            return false;
        }

        if (thingModelExcel.getUnits().length() > 8) {
            ThingModelService.errorExcel.put(thingModelExcel, "物模型功能单位不能超过8个字符");

            return false;
        }

        Map<String, List<String>> brandMap = serailBrandMainboardMap.get(thingModelExcel.getSerial());

        if (CollectionUtils.isEmpty(brandMap)) {
            ThingModelService.errorExcel.put(thingModelExcel, "Serial 超出范围");

            return false;
        }

        List<String> mainboardList = brandMap.get(thingModelExcel.getBrand());

        if (CollectionUtils.isEmpty(mainboardList)) {
            ThingModelService.errorExcel.put(thingModelExcel, "Brand 超出范围");

            return false;
        }

        if (mainboardList.contains(thingModelExcel.getMainboard())) {
            ThingModelService.errorExcel.put(thingModelExcel, "Mainboard 超出范围");

            return false;
        }

        if (typeList.contains(thingModelExcel.getType())) {
            ThingModelService.errorExcel.put(thingModelExcel, "Type 超出范围");

            return false;
        }

        if (isEarlyWarningList.contains(thingModelExcel.getIsEarlyWarning())) {
            ThingModelService.errorExcel.put(thingModelExcel, "IsEarlyWarning 超出范围");

            return false;
        }

        return true;
    }

    private ThingModelSpecs validateSpecs(
            String specs,
            String type,
            Integer isEarlyWarning
    ) throws Exception {
        if (StringUtils.isEmpty(specs)) {
            return new ThingModelSpecs();
        }

        if (type.equals("string")) {
            return JsonUtil.fromJson(specs, TMStringSpecs.class);
        } else if (type.equals("date")) {
            return JsonUtil.fromJson(specs, TMDateSpecs.class);
        } else if (type.equals("array")) {
            return JsonUtil.fromJson(specs, TMArraySpecs.class);
        } else if (type.equals("bool")) {
            Map<Object, Object> tmBoolSpecsMap = JsonUtil.fromJson(specs);

            if (tmBoolSpecsMap.size() > 2) {
                throw new Exception("bool值配置超出范围");
            }

            List<Map<String, String>> boolConfigList = new ArrayList<>();

            tmBoolSpecsMap.forEach((key, value) -> {
                String keyStr = key.toString();
                String valueStr = value.toString();

                if (keyStr.length() > 8) {
                    throw new RuntimeException("bool值key不能超过8个字符");
                }

                if (valueStr.length() > 12) {
                    throw new RuntimeException("bool值value不能超过12个字符");
                }

                Map<String, String> boolMap = new HashMap<>();
                boolMap.put("key", key.toString());
                boolMap.put("value", value.toString());

                boolConfigList.add(boolMap);
            });

            TMBoolSpecs tmBoolSpecs = new TMBoolSpecs();
            tmBoolSpecs.setBoolConfigList(boolConfigList);

            return tmBoolSpecs;
        } else if (type.equals("enum")) {
            Map<Object, Object> tmEnumSpecsMap = JsonUtil.fromJson(specs);

            List<Map<String, String>> enumConfigList = new ArrayList<>();

            tmEnumSpecsMap.forEach((key, value) -> {
                String keyStr = key.toString();
                String valueStr = value.toString();

                if (keyStr.length() > 8) {
                    throw new RuntimeException("枚举值key不能超过8个字符");
                }

                if (valueStr.length() > 12) {
                    throw new RuntimeException("枚举值value不能超过16个字符");
                }

                Map<String, String> boolMap = new HashMap<>();
                boolMap.put("key", key.toString());
                boolMap.put("value", value.toString());

                enumConfigList.add(boolMap);
            });

            TMEnumSpecs tmEnumSpecs = new TMEnumSpecs();
            tmEnumSpecs.setEnumConfigList(enumConfigList);

            return tmEnumSpecs;
        } else if (type.equals("double")) {
            TMDoubleSpecs tmDoubleSpecs = JSONObject.parseObject(specs, TMDoubleSpecs.class);

            if (isEarlyWarning == 1) {
                if (tmDoubleSpecs.getMin() == null || tmDoubleSpecs.getMax() == null) {
                    throw new Exception("物模型功能选择需要预警后，最大值最小值不可为空");
                }
            }

            return tmDoubleSpecs;
        } else if (type.equals("float")) {
            TMFloatSpecs tmFloatSpecs = JSONObject.parseObject(specs, TMFloatSpecs.class);

            if (isEarlyWarning == 1) {
                if (tmFloatSpecs.getMin() == null || tmFloatSpecs.getMax() == null) {
                    throw new Exception("物模型功能选择需要预警后，最大值最小值不可为空");
                }
            }

            return tmFloatSpecs;
        } else if (type.equals("int")) {
            TMIntSpecs tmIntSpecs = JSONObject.parseObject(specs, TMIntSpecs.class);

            if (isEarlyWarning == 1) {
                if (tmIntSpecs.getMin() == null || tmIntSpecs.getMax() == null) {
                    throw new Exception("物模型功能选择需要预警后，最大值最小值不可为空");
                }
            }

            return tmIntSpecs;
        } else {
            return JSONObject.parseObject(specs, ThingModelSpecs.class);
        }
    }

    private void sendIotPropertyTranslate(List<IotPropertyTranslateBo> iptBoList) {
        IotPropertyTranslateBo iptBo = iptBoList.get(0);

        String key = iptBo.getProject() + "/" +
                iptBo.getSerial() + "/" +
                iptBo.getBrand() + "/" +
                iptBo.getMainboard();

        log.info("thingModelProductDetail operate send to data-service,message:" + JSONObject.toJSONString(iptBoList));

        kafkaSender.send(iotPropertyTranslate, key, JSONObject.toJSONString(iptBoList));
    }
}
