package com.xdcplus.workflow.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户自定义去向关系
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xdc_t_user_to_custom_relation")
public class UserToCustomRelation  implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户去向标识
     */
    private Integer userToMark;

    /**
     * 流程配置ID
     */
    private Long configId;

    /**
     * 表达式
     */
    private String expression;

}
