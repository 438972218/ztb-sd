package com.xdcplus.vendor.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BidStructure {

    // 单物品
    SINGLE_ITEM("总额"),

    // 多物品
    MORE_ITEMS("多项产品"),


    ;

    private String value;






}
