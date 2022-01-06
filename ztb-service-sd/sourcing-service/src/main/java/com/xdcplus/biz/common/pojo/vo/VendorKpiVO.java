package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商绩效考核表(VendorKpi)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-28 13:47:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorKpiVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 956490850847806597L;

    @ApiModelProperty("requestId")
    private Long requestId;

    @ApiModelProperty("供应商ID")
    private Long vendorId;

    @ApiModelProperty("供应商名称")
    private String vendorName;

    @ApiModelProperty("负责人")
    private String responsiblePerson;

    @ApiModelProperty("负责人id")
    private Long responsiblePersonId;

    @ApiModelProperty("考核表名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("考核模板")
    private String templateName;

    @ApiModelProperty("考察结果")
    private String result;




    @ApiModelProperty("绩效考核明细")
    private List<VendorKpiDetailVO> vendorKpiDetailVOS;

    @ApiModelProperty("单号")
    private String oddNumber;

    @ApiModelProperty("request状态")
    private String requestStatusName;

    @ApiModelProperty("request状态")
    private String vendorStatus;

    @ApiModelProperty("request")
    private RequestVO requestVO;

    @ApiModelProperty("request标题")
    private String requestTitle;

    @ApiModelProperty("创建人信息")
    private SysUserInfoVO sysUserInfoVO;

    @ApiModelProperty("供应商信息")
    private VendorVO vendorVO;

}
