package springboot.demo.mysql.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import springboot.demo.mysql.model.AuthCompany;

@Mapper
public interface AuthCompanyMapper extends BaseMapper<AuthCompany> {
}
