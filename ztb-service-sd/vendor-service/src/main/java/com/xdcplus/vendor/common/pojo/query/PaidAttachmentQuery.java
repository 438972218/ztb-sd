package com.xdcplus.vendor.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 竞价单附件(PaidAttachment)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:02:20
 */
@Data
@SuppressWarnings("serial")
public class PaidAttachmentQuery implements Serializable {
    private static final long serialVersionUID = -92256122184131864L;

    private Long id;

    private Long paidSheetId;

    private Long paidVendorId;

    private String type;

    private String attachmentType;

    private String attachmentName;

    private String attachmentUrl;

    private String uploadingUser;

    private Long uploadingDate;

    private Integer vendorVisible;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
