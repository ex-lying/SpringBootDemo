package springboot.demo.mysql.vo.specs;

import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

/**
 * @className: TMDoubleSpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午5:47
 */
@Data
public class TMDoubleSpecs extends ThingModelSpecs {
    Integer efficientNum;

    Integer min;

    Integer max;
}
