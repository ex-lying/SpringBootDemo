package springboot.demo.mysql.vo.specs;

import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

/**
 * @className: TMIntSpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午6:00
 */
@Data
public class TMIntSpecs extends ThingModelSpecs {
    Integer min;

    Integer max;
}
