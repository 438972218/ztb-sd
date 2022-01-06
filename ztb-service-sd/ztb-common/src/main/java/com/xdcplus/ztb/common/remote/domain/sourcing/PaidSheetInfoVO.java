package com.xdcplus.ztb.common.remote.domain.sourcing;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价单(PaidSheet)表信息
 *
 * @author Rong.Jia
 * @date 2021/08/17 15:04:28
 */
@Data
public class PaidSheetInfoVO implements Serializable {

    private static final long serialVersionUID = -974810216043725604L;

    private Long id;

    /**
     * requestId
     */
    private Long requestId;

    /**
     * 竞价单号
     */
    private String paidNum;

    /**
     * 竞价单标题
     */
    private String title;

    /**
     * 竞价方向
     */
    private String paidDirection;

    /**
     * 出价结构（总额，多项产品）
     */
    private String bidStructure;

    /**
     * 币种
     */
    private String currency;

    /**
     * 报价开始时间
     */
    private Long offerStartTime;

    /**
     * 报价截止时间
     */
    private Long offerEndTime;

    /**
     * 单号
     */
    private String oddNumber;

    /**
     * request标题
     */
    private String requestTitle;

    /**
     * request状态
     */
    private String requestStatusName;

    /**
     * 供应商状态
     */
    private String vendorStatus;

    /**
     * request
     */
    private RequestVO requestVO;

    /**
     * 所有节点状态
     */
    private List<String> statusNames;

    /**
     * 子表单id
     */
    private Long requestRelationId;

    /**
     * 子表单ToUserId
     */
    private Long requestRelationToUserId;

    /**
     * 子表单ToRoleId
     */
    private Long requestRelationToRoleId;

    /**
     * 物品id
     */
    private Long paidMaterialId;

    /**
     * 用户信息
     */
    private SysUserInfoVO sysUserInfoVO;

    /**
     * 延时间隔
     */
    private Long delayInterval;

    /**
     * 竞价状态，1：进行中，2：暂停，3：已终止，4：截止
     */
    private Integer paidStatusMark;

    private List<PaidMaterialInfoVO> paidMaterialVOS;


}
