package com.xdcplus.vendor.websocket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 竞价排名的信息
 *
 * @author Rong.Jia
 * @date 2021/09/14
 */
@Builder
@Data
public class BidRankingMessage implements Serializable {

    private static final long serialVersionUID = -8315800149232688302L;
    /**
     * 表单ID
     */
    private Long requestId;

    /**
     * 总排名
     */
    private List<BidRanking> totalRanking;

    /**
     *  单排名
     */
    private Map<String, List<BidRanking>> singleRanking;

    /**
     * 竞价排名
     *
     * @author Rong.Jia
     * @date 2021/09/14
     */
    @Data
    public static class BidRanking implements Serializable {

        private static final long serialVersionUID = 8271976748696869252L;
        /**
         * 金额
         */
        @ApiModelProperty("金额")
        private String money;

        /**
         * 出价人
         */
        @ApiModelProperty("出价人")
        private String offerUser;

        /**
         * 排名
         */
        @ApiModelProperty("排名")
        private Integer ranking;

        /**
         * 出价物标识
         */
        @ApiModelProperty("出价物标识")
        private String offerGoods;

        /**
         * 供应商ID
         */
        @ApiModelProperty("供应商ID")
        private Long vendorId;

        /**
         * 供应商标识
         */
        @ApiModelProperty("供应商标识")
        private String vendorMark;

        /**
         * 供应商名称
         */
        @ApiModelProperty("供应商名称")
        private String vendorName;

        /**
         * 出价时间
         */
        @ApiModelProperty("出价时间")
        private Long offerTime;

    }


}
