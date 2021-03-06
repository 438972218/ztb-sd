package com.xdcplus.statement.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;

import java.io.Serializable;

/**
 * 供应商(Vendor)表实体类
 *
 * @author Fish.Fei
 * @since 2021-09-08 14:30:26
 */
@Data
@ApiModel(description = "")
@SuppressWarnings("serial")
@TableName("scm_vendor")
public class Vendor implements Serializable {
    private static final long serialVersionUID = 254842674418801070L;

    @ApiModelProperty("信息主键")
    private Long id;

    @ApiModelProperty("供应商编码")
    private String code;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("营业执照号")
    private String businessLicense;

    @ApiModelProperty("增值税号")
    private String vatNumber;

    @ApiModelProperty("企业邮箱")
    private String enterpriseMail;

    @ApiModelProperty("注册地址")
    private String registeredAddress;

    @ApiModelProperty("主要电话号码")
    private String mainPhone;

    @ApiModelProperty("邮编")
    private String postcode;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("网站")
    private String website;

    @ApiModelProperty("品类")
    private String items;

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
}
