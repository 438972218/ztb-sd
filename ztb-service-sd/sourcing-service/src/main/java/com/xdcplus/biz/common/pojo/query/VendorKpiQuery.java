package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商绩效考核表(VendorKpi)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-28 13:47:35
 */
@Data
@SuppressWarnings("serial")
public class VendorKpiQuery implements Serializable {
    private static final long serialVersionUID = 849889936477737755L;

    private Long id;

    private Long requestId;

    private Long vendorId;

    private String vendorName;

    private String responsiblePerson;

    private Long responsiblePersonId;

    private String name;

    private String remark;

    private String templateName;

    private String result;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;


    private List<Long> requestIds;

    private String keyword;

}
