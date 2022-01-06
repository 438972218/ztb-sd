package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidSheetDTO;
import com.xdcplus.biz.common.pojo.dto.BidSheetFilterDTO;
import com.xdcplus.biz.common.pojo.entity.BidSpecialistScore;
import com.xdcplus.biz.common.pojo.vo.BidSpecialistVO;
import com.xdcplus.biz.generator.BidSheetBaseService;
import com.xdcplus.biz.common.pojo.entity.BidSheet;
import com.xdcplus.biz.common.pojo.vo.BidSheetVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ProcessTransforDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.RequestDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.RequestFlowDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * bid招标单(BidSheet)表服务接口层
 *
 * @author makejava
 * @since 2021-08-12 14:45:42
 */
public interface BidSheetService extends BidSheetBaseService<BidSheet, BidSheetVO, BidSheet> {

    /**
     * 提交招标
     *
     * @param bidSheetDTO 报价表dto
     * @return {@link BidSheetVO}
     */
    BidSheetVO submitBidSheet(BidSheetDTO bidSheetDTO);

    /**
     * 流转招标
     *
     * @param requestFlowDTO 请求流dto
     * @return {@link BidSheetVO}
     */
    BidSheetVO agreeBidSheet(RequestFlowDTO requestFlowDTO);

    /**
     * 退回招标
     *
     * @param bidSheetDTO 报价表dto
     * @return {@link BidSheetVO}
     */
    BidSheetVO backBidSheet(BidSheetDTO bidSheetDTO);

    /**
     * 添加bid招标单(BidSheet)返回VO
     *
     * @param bidSheetDTO bid招标单(BidSheetDTO)
     * @return {@link Boolean} 是否成功
     */
    BidSheetVO saveBidSheetReturnVO(BidSheetDTO bidSheetDTO);

    /**
     * 查询一个并带出附表中数据
     *
     * @param id bid招标单(BidSheet)主键
     * @return {@link BidSheetVO} bid招标单(BidSheet)信息
     */
    BidSheetVO showBidSheetByRequestId(Long id);

    /**
     * 查询招标单(request)(BidSheet)
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetVO  >} 状态信息
     */
    PageVO<BidSheetVO> queryBidSheetWithRequest(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 查询招标单(request)(BidSheet)2
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetVO  >} 状态信息
     */
    PageVO<BidSheetVO> queryBidSheetWithRequest2(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 查询招标单(request)(BidSheet)3
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetVO  >} 状态信息
     */
    PageVO<BidSheetVO> queryBidSheetWithRequest3(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 查询招标单(request)(BidSheet)4
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetVO  >} 状态信息
     */
    PageVO<BidSheetVO> queryBidSheetWithRequest4(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 查询招标单模板(request)(BidSheet)
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetVO  >} 状态信息
     */
    PageVO<BidSheetVO> queryBidSheetTemplate(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 选择数量项目id
     *
     * @param id id
     * @return int
     */
    int selectCountByProjectId(Long id);


    /**
     * 提交前法官地位
     *
     * @param bidSheetDTO 报价表dto
     * @param requestDTO  请求dto
     */
    void submitBeforeJudgeStatus(BidSheetDTO bidSheetDTO, RequestDTO requestDTO);

    /**
     * 提交后,法官的地位
     *
     * @param bidSheetDTO 报价表dto
     */
    void submitAfterJudgeStatus(BidSheetDTO bidSheetDTO);

    /**
     * 流转之前的操作
     *
     * @param requestFlowDTO 请求流dto
     * @return {@link ProcessTransforDTO}
     */
    ProcessTransforDTO approveBeforeJudgeStatus(RequestFlowDTO requestFlowDTO);

    /**
     * 流转之后的操作
     *
     * @param requestFlowDTO 请求流dto
     */
    void approveAfterJudgeStatus(RequestFlowDTO requestFlowDTO);

    /**
     * 插入收购专家评分
     *
     * @param bidSpecialistVOS    收购专家vos
     * @param bidSheetVO          报价表签证官
     * @param bidVendorId         投标供应商id
     * @param bidVendorName       投标供应商名称
     * @param bidSpecialistScores 收购专业分数
     * @return {@link List<BidSpecialistScore>}
     */
    List<BidSpecialistScore> insertBidSpecialistScore(List<BidSpecialistVO> bidSpecialistVOS, BidSheetVO bidSheetVO,
                                                      long bidVendorId, String bidVendorName, List<BidSpecialistScore> bidSpecialistScores);

    /**
     * 更新报价表马克
     *
     * @param bidSheetVO 报价表签证官
     * @param mark       马克
     * @return {@link Boolean}
     */
    Boolean updateBidSheetMark(BidSheetVO bidSheetVO, String mark);


    /**
     * 结束投标
     */
    void finishBidSheet();

}
