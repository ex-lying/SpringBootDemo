package springboot.demo.script.thingModel.controller;

import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.demo.excel.ExcelUtil;
import springboot.demo.excel.ThingModelExcelListener;
import springboot.demo.excel.model.ThingModelExcel;
import springboot.demo.script.thingModel.service.ThingModelService;
import tool.util.JsonUtil;
import tool.util.LogUtil;

import java.io.File;

/**
 * @className: ReadExcelToMysql
 * @author: Lying
 * @description: TODO
 * @date: 2023/4/6 上午10:23
 */
@RestController
@Validated
public class ThingModelExcelController {
    private final static LogUtil log = new LogUtil(ThingModelExcelController.class);

    @Autowired
    ThingModelService thingModelService;

    @GetMapping("/thingModel/readExcel")
    public void readExcel(@RequestParam String sourcePath) {
        if(StringUtils.isEmpty(sourcePath)){
            sourcePath = "src/main/resources/thingModelExcel";
        }

        File fileNew = new File(sourcePath);

        File[] files = fileNew.listFiles();

        for (File file : files) {
            if (ExcelUtil.checkExcelExtension(file)) {
                log.info("开始处理表格:"+file.getPath());

                EasyExcel.read(file.getPath(), ThingModelExcel.class, new ThingModelExcelListener(thingModelService)).sheet().doRead();
            }
        }

        log.error("添加失败的物模型:"+ JsonUtil.toJson(ThingModelService.errorExcel));
    }
}
