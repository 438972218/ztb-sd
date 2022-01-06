package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 招标附件(VendorAttachment)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:01
 */
@Data
@SuppressWarnings("serial")
public class VendorAttachmentQuery implements Serializable {
    private static final long serialVersionUID = 449088856757910924L;

    private Long id;

    private Long vendorId;

    private String type;

    private String attachmentType;

    private String attachmentName;

    private String attachmentUrl;

    private String uploadingUser;

    private Long uploadingDate;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
