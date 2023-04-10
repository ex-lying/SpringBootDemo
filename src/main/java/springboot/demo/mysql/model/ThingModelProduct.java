package springboot.demo.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @className: ThingModelProduct
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/27 上午11:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThingModelProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer thingModelId;

    private String serial;

    private String brand;

    private String mainboard;

    //是否有效：0，无效；1，有效
    private int isEfficient;

    private LocalDateTime updateTime;

    private String updateUser;

    private LocalDateTime createTime;

    private String createUser;
}
