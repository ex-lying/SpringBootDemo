package springboot.demo.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import springboot.demo.mysql.model.ThingModelProduct;

/**
 * @className: ThingModelProduct
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/27 下午2:02
 */
@Mapper
public interface ThingModelProductMapper extends BaseMapper<ThingModelProduct> {
}
