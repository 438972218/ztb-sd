package com.xdcplus.vendor.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公告牌信息
 *
 * @author Rong.Jia
 * @since 2021-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xdc_t_bulletin_board")
public class BulletinBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 公告类型(1->全局公告，2->表单公告)
     */
    private Byte type;

    /**
     * 表单ID(全局公告->-1)
     */
    private Long requestId;

    /**
     * 内容
     */
    private String message;

    /**
     * 公告时间
     */
    private Long bulletinTime;

    /**
     * 添加时间
     */
    private Long createdTime;

    /**
     * 修改时间
     */
    private Long updatedTime;

    /**
     * 添加人
     */
    private String createdUser;

    /**
     * 修改人
     */
    private String updatedUser;

    /**
     * 描述
     */
    private String description;


}
