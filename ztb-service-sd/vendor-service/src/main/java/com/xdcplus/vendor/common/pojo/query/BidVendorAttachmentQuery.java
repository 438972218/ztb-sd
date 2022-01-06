package com.xdcplus.vendor.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 招标供应商附件(BidVendorAttachment)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:19
 */
@Data
@SuppressWarnings("serial")
public class BidVendorAttachmentQuery implements Serializable {
    private static final long serialVersionUID = 756406046570754496L;

    private Long id;

    private Long bidSheetId;

    private Long bidVendorId;

    private String bidType;

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
