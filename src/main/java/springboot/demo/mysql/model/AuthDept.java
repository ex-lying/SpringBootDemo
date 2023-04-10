package springboot.demo.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("auth_dept")
@NoArgsConstructor
@AllArgsConstructor
public class AuthDept {


    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 组织架构父子路径
     */
    private String deptPath;
    /**
     * 部门名字
     */
    private String deptName;
    /**
     * 排序
     */
    private Integer deptSort;
    /**
     * 公司名字
     */
    private String companyName;
    /**
     * 对外唯一标志
     */
    private String uniqueId;
    /**
     * 状态（enable启用 stopUsing停用）
     */
    private String status;
    /**
     * 公司id
     */
    private Long companyId;

}
