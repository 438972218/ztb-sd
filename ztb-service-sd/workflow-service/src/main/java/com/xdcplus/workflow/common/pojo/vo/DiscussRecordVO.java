package com.xdcplus.workflow.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * 讨论记录VO
 *
 * @author Rong.Jia
 * @date 2021/08/18 17:13:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("讨论记录信息对照对象")
public class DiscussRecordVO extends BaseBO implements Serializable {

    private static final long serialVersionUID = 3319887944101018045L;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 发信人
     */
   private CommunicationPersonBean from;

    /**
     * 收信人
     */
    @ApiModelProperty("收信人")
    private List<CommunicationPersonBean> tos;

    /**
     * 讨论组信息
     */
    @ApiModelProperty("讨论组信息")
    private DiscussGroupVO discussGroup;

    /**
     *  通信人信息
     */
    @ApiModel("通信人信息")
    @Getter
    @Builder
    public static class CommunicationPersonBean implements Serializable {

        private static final long serialVersionUID = 4266255789060161105L;

        /**
         * 通信人
         */
        @ApiModelProperty("通信人")
        private String account;

        /**
         * 通信人名称
         */
        @ApiModelProperty("通信人名称")
        private String name;

    }




}
