package springboot.demo.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @className: ThingModelExcel
 * @author: Lying
 * @description: TODO
 * @date: 2023/4/6 上午10:56
 */
@Data
public class ThingModelExcel {
    @ExcelProperty(value = "Project", index = 0)
    private String Project;

    @ExcelProperty(value = "Serial", index = 1)
    private String Serial;

    @ExcelProperty(value = "Brand", index = 2)
    private String Brand;

    @ExcelProperty(value = "Mainboard", index = 3)
    private String Mainboard;

    @ExcelProperty(value = "Type", index = 4)
    private String Type;

    @ExcelProperty(value = "Identifier", index = 5)
    private String Identifier;

    @ExcelProperty(value = "Name", index = 6)
    private String Name;

    @ExcelProperty(value = "Specs", index = 7)
    private String Specs;

    @ExcelProperty(value = "ExpectedValue", index = 8)
    private String ExpectedValue;

    @ExcelProperty(value = "Units", index = 9)
    private String Units;

    @ExcelProperty(value = "Order", index = 11)
    private Integer Order;

    @ExcelProperty(value = "IsEarlyWarning", index = 10)
    private Integer IsEarlyWarning;
}
