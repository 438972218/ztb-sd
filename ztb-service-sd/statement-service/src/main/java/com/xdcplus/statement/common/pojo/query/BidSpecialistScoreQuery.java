package com.xdcplus.statement.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 招标专家评分(BidSpecialistScore)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-10-27 13:48:41
 */
@Data
@SuppressWarnings("serial")
public class BidSpecialistScoreQuery implements Serializable {
    private static final long serialVersionUID = 625844175440132610L;

    private Long id;

    private Long bidVendorId;

    private String bidVendorName;

    private Long userId;

    private String bidType;

    private Integer whetherView;

    private Long viewTime;

    private Double score;

    private String comment;

    private Integer round;

    private String status;

    private Long commentTime;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;


    private List<Long> bidVendorIds;

}
