package springboot.demo.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import springboot.demo.mysql.model.ThingModel;

/**
 * @className: ThingModelMapper
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/24 上午10:56
 */
@Mapper
public interface ThingModelMapper extends BaseMapper<ThingModel> {
}
