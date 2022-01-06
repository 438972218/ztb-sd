package com.xdcplus.vendor.common.pojo.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 出价物品
 *
 * @author Rong.Jia
 * @date 2021/09/18
 */
@Data
public class BidItemsBO implements Serializable {

    /**
     * 出价人
     */
    private String offerUser;

    /**
     * 出价物标识
     */
    private String offerGoods;

}
