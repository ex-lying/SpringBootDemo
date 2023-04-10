package springboot.demo.mysql.vo.specs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

/**
 * @className: TMArraySpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午5:16
 */
@Data
public class TMArraySpecs extends ThingModelSpecs {
    @NotNull(message = "元素类型不能为空")
    private String type;
    private Integer length;
}
