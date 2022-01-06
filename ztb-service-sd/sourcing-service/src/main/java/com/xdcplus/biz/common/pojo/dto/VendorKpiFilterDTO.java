package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商绩效考核表(VendorKpi)表查询入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-28 13:47:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorKpiFilterDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = 651844479847369752L;

    @ApiModelProperty("信息主键")
    private Long id;

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

    @ApiModelProperty("创建人")
    private String createdUser;

    @ApiModelProperty("创建时间")
    private Long createdTime;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("修改时间")
    private Long updatedTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("是否已经逻辑删除（0：未删除 1：已删除）")
    private Integer deleted;


    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("表单状态id")
    private Long statusId;

    @ApiModelProperty(value = "单号 可模糊查询")
    private String oddNumber;

    @ApiModelProperty("requestIds")
    private List<Long> requestIds;

}
