package springboot.demo.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @className: ThingModelProductDetail
 * @author: Lying
 * @description: TODO
 * @date: 2023/3/27 上午11:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThingModelProductDetail implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer ThingModelProductId;

    private String name;

    private int sort;

    private String identifier;

    private String type;

    private String specs;

    private String category;

    private String expectedValue;

    private String units;

    //是否预警：0，不预警，1，预警
    private Integer isEarlyWarning;

    private String mark;

    //是否有效：0，无效；1，有效
    private int isEfficient;

    private LocalDateTime updateTime;

    private String updateUser;

    private LocalDateTime createTime;

    private String createUser;
}
