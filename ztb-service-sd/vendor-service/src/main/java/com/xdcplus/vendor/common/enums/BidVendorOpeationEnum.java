package com.xdcplus.vendor.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 投标供应商操作枚举
 *
 * @author Fish.Fei
 * @date 2021/09/29
 */
@AllArgsConstructor
@Getter
public enum BidVendorOpeationEnum {

    // 已回复
    REPLY("已回复"),

    // 已拒绝
    refuse("已拒绝"),

    // 已撤回
    RECALL("已撤回"),

    ;


    private String value;

}
