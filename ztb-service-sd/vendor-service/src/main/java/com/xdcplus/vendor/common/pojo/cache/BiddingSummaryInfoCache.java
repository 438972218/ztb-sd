package com.xdcplus.vendor.common.pojo.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 投标简要信息
 *
 * @author Rong.Jia
 * @date 2021/09/14
 */
@Data
public class BiddingSummaryInfoCache implements Serializable {

    /**
     * 表单ID
     */
    private Long requestId;

    /**
     * 物品标识
     */
    private String offerGoods;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 竞价状态，1：进行中，2：暂停，3：已终止，4：截止
     */
    private Integer paidStatus;

    /**
     * 竞价方向 true: 正向，false: 反向
     */
    private Boolean paidDirection;

    /**
     * 是否是多物品竞价，true: 是，false: 否
     */
    private Boolean moreItems;

    /**
     *  竞价单内的竞价物品总数量
     */
    private Integer totalQuantityArticles;

    /**
     * 招标员用户ID
     */
    private Long tendererUserId;

    /**
     * 延时间隔
     */
    private Long delayInterval;

    /**
     *  供应商摘要信息
     */
    private Set<VendorSummary> vendorSummaries;

    /**
     *
     * 供应商摘要
     * @author Rong.Jia
     * @date 2021/09/14
     */
    @Data
    public static class VendorSummary implements Serializable {

        /**
         * 供应商id
         */
        private Long vendorId;

        /**
         * 供应商用户id
         */
        private Long vendorUserId;

        /**
         * 供应商用户名
         */
        private String vendorUserName;

        /**
         * 供应商名称
         */
        private String vendorName;

        /**
         * 供应商标识
         */
        private String vendorMark;

    }
















}
