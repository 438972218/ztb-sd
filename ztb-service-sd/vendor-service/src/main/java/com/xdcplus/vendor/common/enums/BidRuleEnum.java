package com.xdcplus.vendor.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出价规则枚举
 *
 * @author Rong.Jia
 * @date 2021/09/10
 */
@AllArgsConstructor
@Getter
public enum BidRuleEnum {

    // 绝对阈值
    ABSOLUTE_THRESHOLD("绝对阈值"),

    // 最小出价倍数
    MULTIPLE("最小出价倍数"),



    ;


    private String value;

}
