package springboot.demo.mysql.vo.specs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import springboot.demo.mysql.vo.ThingModelSpecs;

import java.util.List;
import java.util.Map;

/**
 * @className: TMEnumSpecs
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/29 下午5:39
 */

@Data
public class TMEnumSpecs extends ThingModelSpecs {
    @NotNull(message = "枚举项配置列表")
    List<Map<String, String>> enumConfigList;
}
