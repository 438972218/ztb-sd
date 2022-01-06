package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.excel.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@ApiModel(description = "")
public class VendorExcelDTO implements Serializable {

    @ExcelField(title="supplierActivation", align=2)
    @ApiModelProperty("供应商激活")
    private String supplierActivation;

    @ExcelField(title="companyAccountManagement", align=2)
    @ApiModelProperty("由买方管理的供应商账户")
    private String companyAccountManagement;

    @ExcelField(title="userAlias", align=2)
    @ApiModelProperty("账号")
    private String userAlias;

    @ExcelField(title="companyCountry", align=2)
    @ApiModelProperty("国家")
    private String country;

    @ExcelField(title="companyName", align=2)
    @ApiModelProperty("企业名称")
    private String companyName;

    @ExcelField(title="companyFiscalCode", align=2)
    @ApiModelProperty("营业执照号")
    private String businessLicense;

    @ExcelField(title="companyVat", align=2)
    @ApiModelProperty("增值税号")
    private String vatNumber;

    @ExcelField(title="companyAddress", align=2)
    @ApiModelProperty("注册地址")
    private String registeredAddress;

    @ExcelField(title="companyZip", align=2)
    @ApiModelProperty("邮编")
    private String postcode;

    @ExcelField(title="companyCity", align=2)
    @ApiModelProperty("城市")
    private String city;

    @ExcelField(title="companyWebSite", align=2)
    @ApiModelProperty("网站")
    private String website;

    @ExcelField(title="userSurname", align=2)
    private String userSurname;

    @ExcelField(title="userName", align=2)
    private String userName;

    @ExcelField(title="userEmail", align=2)
    private String userEmail;

    @ExcelField(title="userPhone", align=2)
    private String userPhone;

//    @ExcelField(title="userMobilePhone", align=2)
//    private String userMobilePhone;
//
//    @ExcelField(title="userFax", align=2)
//    private String userFax;
//
//    @ExcelField(title="userRole", align=2)
//    private String userRole;
}
