package springboot.demo.excel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.demo.SpringBootDemoApplication;
import springboot.demo.script.thingModel.controller.ThingModelExcelController;

/**
 * @className: ThingModelExcelTest
 * @author: Lying
 * @description: TODO
 * @date: 2023/4/6 下午2:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class ThingModelExcelTest {
    @Autowired
    ThingModelExcelController readExcelToMysql;

    @Test
    public void readThingModelExcel(){
        readExcelToMysql.readExcel(null);
    }
}
