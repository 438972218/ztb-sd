package com.xdcplus.vendor.websocket;

import lombok.Data;

/**
 * websocket 发送消息
 *
 * @author Rong.Jia
 * @date 2021/08/17 11:50:44
 */
@Data
public class SendMessage {

    /**
     * 发送人
     */
    private String fromUserId;

    /**
     *  消息类型
     *  1：出价信息
     *  2：供应商状态,竞价状态
     *  3: 竞价时间
     *  4: 公告信息
     *  5：出价排名
     *  -1: 通知供应商标识刷新信息
     */
    private Integer type;

    /**
     * 消息
     */
    private Object message;




}
