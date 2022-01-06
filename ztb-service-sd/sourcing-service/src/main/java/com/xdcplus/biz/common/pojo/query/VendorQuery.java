package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商(Vendor)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-08 14:30:26
 */
@Data
@SuppressWarnings("serial")
public class VendorQuery implements Serializable {
    private static final long serialVersionUID = 637937377138662166L;

    private Long id;

    private String code;

    private String status;

    private String name;

    private String businessLicense;

    private String vatNumber;

    private String enterpriseMail;

    private String registeredAddress;

    private String mainPhone;

    private String postcode;

    private String city;

    private String country;

    private String website;

    private String items;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

    private String nameKeyword;

    private String codeKeyword;

}
