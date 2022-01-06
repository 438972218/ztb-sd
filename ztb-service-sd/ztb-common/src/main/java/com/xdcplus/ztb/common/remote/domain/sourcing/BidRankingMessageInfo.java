package com.xdcplus.ztb.common.remote.domain.sourcing;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 竞价排名的信息
 *
 * @author Fish.Fei
 */
@EqualsAndHashCode()
@Data
public class BidRankingMessageInfo implements Serializable {

    private static final long serialVersionUID = -7377947933049306520L;
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

}
