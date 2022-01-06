package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidSheetFilterDTO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.biz.common.pojo.vo.BidSheetVO;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.biz.generator.PaidSheetBaseService;
import com.xdcplus.biz.common.pojo.entity.PaidSheet;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.RequestFlowDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 竞价单(PaidSheet)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:03
 */
public interface PaidSheetService extends PaidSheetBaseService<PaidSheet, PaidSheetVO, PaidSheet> {

    /**
     * 提交竞价
     *
     * @param paidSheetDTO 工资表dto
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO submitPaidSheet(PaidSheetDTO paidSheetDTO);

    /**
     * 流转竞价
     *
     * @param requestFlowDTO 请求流dto
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO agreePaidSheet(RequestFlowDTO requestFlowDTO);

    /**
     * 添加竞价单(PaidSheet)返回VO
     *
     * @param paidSheetDTO 竞价单(PaidSheetDTO)
     * @return {@link Boolean} 是否成功
     */
    PaidSheetVO savePaidSheetReturnVO(PaidSheetDTO paidSheetDTO);

    /**
     * 显示支付表请求id
     *
     * @param id id
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO showPaidSheetByRequestId(Long id);

    /**
     * 显示支付表请求id1
     *
     * @param id id
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO showPaidSheetById(Long id);

    /**
     * 查询与request支付表
     *
     * @param paidSheetFilterDTO 工资表过滤dto
     * @return {@link PageVO<PaidSheetVO>}
     */
    PageVO<PaidSheetVO> queryPaidSheetWithRequest(PaidSheetFilterDTO paidSheetFilterDTO);

    /**
     * 查询与request支付表1
     *
     * @param paidSheetFilterDTO 工资表过滤dto
     * @return {@link PageVO<PaidSheetVO>}
     */
    PageVO<PaidSheetVO> queryPaidSheetWithRequest1(PaidSheetFilterDTO paidSheetFilterDTO);

    /**
     * 查询支付表的id与项目
     *
     * @param id id
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO queryPaidSheetWithProjectById(Long id);

    /**
     * 将进行中的竞价单存入redis中
     */
    void queryPaidSheetByMonitor();

    /**
     * 开始支付表
     */
    void startPaidSheet();

    /**
     * 查询出项目下的竞价单数量
     *
     * @param id id
     * @return int
     */
    int selectCountByProjectId(Long id);

    /**
     * 暂停支付表
     *
     * @param id id
     * @return {@link Boolean}
     */
    Boolean pausePaidSheet(Long id);

    /**
     * 恢复支付表
     *
     * @param id id
     * @return {@link Boolean}
     */
    Boolean recoverPaidSheet(Long id);

    /**
     * 重新竞价
     *
     * @param paidSheetDTO 工资表dto
     * @return {@link Boolean}
     */
    Boolean anewPaidSheet(PaidSheetDTO paidSheetDTO);

    /**
     * 终止支付表
     *
     * @param id id
     * @return {@link Boolean}
     */
    Boolean terminatePaidSheet(Long id);

    /**
     * 查询竞价单模板(request)(PaidSheet)
     *
     * @param paidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<   PaidSheetVO   >} 状态信息
     */
    PageVO<PaidSheetVO> queryPaidSheetTemplate(PaidSheetFilterDTO paidSheetFilterDTO);

    /**
     * 审批之后的操作
     *
     * @param requestFlowDTO 请求流dto
     */
    void approveAfterJudgeStatus(RequestFlowDTO requestFlowDTO);

    void refreshPaidVendorMark();

    /**
     * 判断是否授权
     */
    void judgeAuthorization(Long paidSheetId);


}
