package springboot.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import springboot.demo.excel.model.ThingModelExcel;
import springboot.demo.script.thingModel.service.ThingModelService;
import tool.util.JsonUtil;
import tool.util.LogUtil;

import java.util.Map;

/**
 * @className: ExcelListener
 * @author: Lying
 * @description: TODO
 * @date: 2023/4/6 上午10:54
 */
public class ThingModelExcelListener extends AnalysisEventListener<ThingModelExcel> {
    private final static LogUtil log = new LogUtil(ThingModelExcelListener.class);

    private ThingModelService thingModelService;

    public ThingModelExcelListener(ThingModelService thingModelService) {
        this.thingModelService = thingModelService;
    }

    @Override
    public void invoke(ThingModelExcel thingModelExcel, AnalysisContext analysisContext) {
        log.info("物模型表格内容:" + JsonUtil.toJson(thingModelExcel));

        thingModelService.insertThingModel(thingModelExcel);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);

        log.info("物模型表头:" + JsonUtil.toJson(headMap));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("物模型表格读取后执行操作。");
    }

}
