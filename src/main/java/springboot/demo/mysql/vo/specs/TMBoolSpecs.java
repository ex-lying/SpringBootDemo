package springboot.demo.mysql.vo.specs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

import java.util.List;
import java.util.Map;

/**
 * @className: TMBoolSpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午5:23
 */
@Data
public class TMBoolSpecs extends ThingModelSpecs {
    @NotNull(message = "bool值配置列表不可为空")
    private List<Map<String, String>> boolConfigList;
}
