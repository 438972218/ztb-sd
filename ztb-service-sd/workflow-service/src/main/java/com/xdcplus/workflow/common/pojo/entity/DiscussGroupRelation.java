package com.xdcplus.workflow.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 讨论组关联信息
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xdc_t_discuss_group_relation")
public class DiscussGroupRelation implements Serializable {

    private static final long serialVersionUID = -3813226021717640951L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 讨论组ID
     */
    private Long groupId;

    /**
     * 用户账号
     */
    private String toUser;























}
