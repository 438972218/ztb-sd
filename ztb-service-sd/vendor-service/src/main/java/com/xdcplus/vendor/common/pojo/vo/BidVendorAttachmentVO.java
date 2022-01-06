package com.xdcplus.vendor.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 招标供应商附件(BidVendorAttachment)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorAttachmentVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -81107160747807465L;

    @ApiModelProperty("招标单id")
    private Long bidSheetId;

    @ApiModelProperty("招标供应商id")
    private Long bidVendorId;

    @ApiModelProperty("标类型(商业/技术/资质)")
    private String bidType;

    @ApiModelProperty("附件类型")
    private String attachmentType;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentUrl;

    @ApiModelProperty("上传人")
    private String uploadingUser;

    @ApiModelProperty("上传时间")
    private Long uploadingDate;

    @ApiModelProperty("招标供应商信息")
    private BidVendorVO bidVendorVO;

    @ApiModelProperty("用户信息")
    private SysUserInfoVO sysUserInfoVO;

}
