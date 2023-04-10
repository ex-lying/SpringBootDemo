package springboot.demo.mysql.vo.specs;

import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

/**
 * @className: TMFloatSpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午5:50
 */
@Data
public class TMFloatSpecs extends ThingModelSpecs {
    private Integer efficientNum;

    Integer min;

    Integer max;
}
