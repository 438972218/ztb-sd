package com.xdcplus.vendor.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 招标供应商状态枚举
 *
 * @author Fish.Fei
 * @date 2021/09/16
 */

@Getter
@AllArgsConstructor
public enum BidVendorStatusEnum {

    // 待回复
    TO_REPLY("待回复"),

    // 未回复
    NO_REPLY("未回复"),

    // 已回复
    HAS_REPLY("已回复"),

    // 已参与
    HAS_PARTICIPATE("已参与"),

    // 已拒绝
    HAS_REFUSED("已拒绝"),

    // 已撤回
    HAS_WITHDRAWN("已撤回"),

    ;

    private String value;
}
