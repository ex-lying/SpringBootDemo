package springboot.demo.mysql.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("auth_company")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCompany {


    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 公司名字
     */
    private String companyName;
    /**
     * 公司简称
     */
    private String companyAbbreviation;
    /**
     * 公司logo
     */
    private String companyLogo;
    /**
     * （enable启用 stopUsing停用）
     */
    private String status;
    /**
     * 排序
     */
    private Integer companySort;

    /**
     * 故障分类标准id
     */
    private int faultStandardId;

    /**
     * 物模型标准id
     */
    private int propertyStandardId;


    public LocalDateTime createTime;

    /**
     * 更新时间
     */

    public LocalDateTime updateTime;

    /**
     * 创建者
     */
    public String createBy;

    /**
     * 更新者
     */
    public String updateBy;

}
