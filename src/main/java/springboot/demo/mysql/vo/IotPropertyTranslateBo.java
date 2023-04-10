package springboot.demo.mysql.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @className: IotPropertyTranslateBo
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/30 上午10:29
 */
@Data
@Accessors(chain = true)
@ToString
public class IotPropertyTranslateBo {
    private String project;
    private String originProject;
    private String serial;
    private String originSerial;
    private String brand;
    private String originBrand;
    private String mainboard;
    private String originMainboard;
    private String identifier;
    private String originIdentifier;
    private String name;
    private Integer sort;
    private String specs;
    private String type;
    private String expectedValue;
    private String units;
    private Integer isEarlyWarning;
    private String operateType;
}
