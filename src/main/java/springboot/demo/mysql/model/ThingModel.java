package springboot.demo.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @className: ThingModel
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/24 上午11:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThingModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String project;

    private String mark;

    //是否默认：0，不是：1，是
    private int isDefault;

    //是否有效：0，无效；1，有效
    private int isEfficient;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String updateUser;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String createUser;
}
