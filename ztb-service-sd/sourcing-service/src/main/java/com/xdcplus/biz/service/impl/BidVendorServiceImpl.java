package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.common.pojo.entity.BidSpecialistScore;
import com.xdcplus.biz.common.pojo.entity.BidVendorDetail;
import com.xdcplus.biz.common.pojo.query.BidVendorDetailQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.impl.BidVendorBaseServiceImpl;
import com.xdcplus.biz.mapper.BidVendorMapper;
import com.xdcplus.biz.common.pojo.entity.BidVendor;
import com.xdcplus.biz.service.*;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 招标投标方(BidVendor)表服务实现类
 *
 * @author makejava
 * @since 2021-08-12 14:45:47
 */
@Slf4j
@Service("bidVendorService")
public class BidVendorServiceImpl extends BidVendorBaseServiceImpl<BidVendor, BidVendorVO, BidVendor, BidVendorMapper> implements BidVendorService {

    @Autowired
    private BidDetailService bidDetailService;

    @Autowired
    private BidVendorDetailService bidVendorDetailService;

    @Autowired
    private BidSpecialistScoreService bidSpecialistScoreService;

    @Autowired
    private BidSheetService bidSheetService;

    @Autowired
    private BidSpecialistService bidSpecialistService;

    @Autowired
    private VendorService vendorService;

    @Override
    public BidVendorVO saveBidVendorWithDetailReturnVO(BidVendorDTO bidVendorDTO) {
        List<BidVendor> bidVendors = bidVendorMapper.selectList(new QueryWrapper<BidVendor>()
                .lambda().eq(BidVendor::getBidSheetId, bidVendorDTO.getBidSheetId())
                .eq(BidVendor::getVendorId, bidVendorDTO.getVendorId())
                .eq(BidVendor::getDeleted, NumberConstant.ZERO));
        if (CollectionUtil.isNotEmpty(bidVendors)) {
            log.error("saveBidVendorWithDefaultReturnVO() bidVendors exist");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_EXIST);
        }

        long sheetId = bidVendorDTO.getBidSheetId();
        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(sheetId);
        List<BidDetailVO> bidDetailVOS = bidDetailService.queryBidDetailVOList(bidDetailFilterDTO);
        if (CollectionUtil.isEmpty(bidDetailVOS)) {
            log.error("saveBidVendorWithDetail() BidDetailVO select faild");
            throw new ZtbWebException(ResponseEnum.BID_DETAIL_SELECT_ERROR);
        }

        BidVendorVO bidVendorVO = saveBidVendorWithDefaultReturnVO(bidVendorDTO);
        if (ObjectUtil.isEmpty(bidVendorVO)) {
            log.error("saveBidVendorWithDetail() BidVendor insert faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_INSERT_FAIL);
        }
        long id = bidVendorVO.getId();

        List<BidVendorDetail> bidVendorDetails = CollectionUtil.newArrayList();
        bidDetailVOS.forEach(bidDetailVO -> {
            BidVendorDetail bidVendorDetail = BeanUtil.copyProperties(bidDetailVO, BidVendorDetail.class);
            bidVendorDetail.setId(null);
            bidVendorDetail.setBidVendorId(id);
            bidVendorDetail.setRound(NumberConstant.ZERO);
            bidVendorDetails.add(bidVendorDetail);
        });
        Boolean result = bidVendorDetailService.saveOrUpdateBatch(bidVendorDetails);
        if (result) {
            return bidVendorVO;
        } else {
            return null;
        }

    }

    @Override
    public BidVendorVO saveBidVendorWithDefaultReturnVO(BidVendorDTO bidVendorDTO) {
        List<BidVendor> bidVendors = bidVendorMapper.selectList(new QueryWrapper<BidVendor>()
                .lambda().eq(BidVendor::getBidSheetId, bidVendorDTO.getBidSheetId())
                .eq(BidVendor::getVendorId, bidVendorDTO.getVendorId())
                .eq(BidVendor::getDeleted, NumberConstant.ZERO));
        if (CollectionUtil.isNotEmpty(bidVendors)) {
            log.error("saveBidVendorWithDefaultReturnVO() bidVendors exist");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_EXIST);
        }

        BidVendor bidVendor = BeanUtil.copyProperties(bidVendorDTO, BidVendor.class);
        bidVendor.setCreatedTime(DateUtil.current());
        bidVendor.setDeleted(0);
        bidVendor.setAgainStatus(ZtbConstant.INVITED);
        bidVendor.setVendorStatus(ZtbConstant.VENDOR_NO_REPLIED);
        bidVendor.setRound(NumberConstant.ZERO);
        bidVendor.setQualificationAttQuantity(NumberConstant.ZERO);
        bidVendor.setTechnologyAttQuantity(NumberConstant.ZERO);
        bidVendor.setCommerceScore(Double.valueOf(NumberConstant.ZERO));
        bidVendor.setQualificationScore(Double.valueOf(NumberConstant.ZERO));
        bidVendor.setTechnologyScore(Double.valueOf(NumberConstant.ZERO));

        boolean result = this.save(bidVendor);
        BidVendorVO bidVendorVO = BeanUtil.copyProperties(bidVendor, BidVendorVO.class);
        if (result) {
            return bidVendorVO;
        } else {
            return null;
        }
    }

    @Override
    public void returnBidVendor(BidSheetDTO bidSheetDTO) {
        Long bidSheetId =bidSheetDTO.getId();
        List<BidVendorDTO> bidVendorDTOS=bidSheetDTO.getBidVendorDTOS();
        BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetId);
        //修改bidSheet标识:澄清报价/最终报价,澄清截止时间
        BidSheetDTO bidSheetDTONew=BeanUtil.copyProperties(bidSheetVO,BidSheetDTO.class);
        bidSheetDTONew.setMark(bidSheetDTO.getMark());
        bidSheetDTONew.setClarifyEndTime(bidSheetDTO.getClarifyEndTime());
        bidSheetService.updateBidSheet(bidSheetDTONew);


        BidSpecialistFilterDTO bidSpecialistFilterDTO = new BidSpecialistFilterDTO();
        bidSpecialistFilterDTO.setBidSheetId(bidSheetId);
        List<BidSpecialistVO> bidSpecialistVOS = bidSpecialistService.queryBidSpecialistVOList(bidSpecialistFilterDTO);
        if (CollectionUtil.isEmpty(bidSpecialistVOS)) {
            log.error("approveBeforeJudgeStatus() bidSpecialistVOS select faild");
            throw new ZtbWebException(ResponseEnum.BID_SPECIALIST_SELECT_FAIL);
        }

        for (BidVendorDTO bidVendorDTO : bidVendorDTOS) {

            long bidVendorId = bidVendorDTO.getId();
            String againStatus = bidVendorDTO.getAgainStatus();

            //更新供应商重开RFQ状态,轮次,查看RFQ时间清空
            BidVendor bidVendor = resetBidVendor(bidVendorId, againStatus);

            if (ZtbConstant.INVITED.equals(againStatus)) {
                //受邀，更新提交状态的明细改为历史，并新增保存状态的明细,轮次+1
                //修改原评分状态为历史,并新增最新状态的专家评分
                resetBidVendorDetail(bidSheetId, bidVendor);

                //修改原评分状态为历史,并新增最新状态的专家评分
                resetBidSpecialistScore(bidVendor);
            }
        }
    }

    @Override
    public PageVO<BidVendorVO> queryBidVendorWithVendor(BidVendorFilterDTO bidVendorFilterDTO) {
        PageVO<BidVendorVO> pageVO = queryBidVendor(bidVendorFilterDTO);
        List<BidVendorVO> bidVendorVOS = pageVO.getRecords();


        if (CollectionUtil.isNotEmpty(bidVendorVOS)) {
            bidVendorVOS.forEach(bidVendorVO -> {
                if (bidVendorFilterDTO.getSpecialistUserId() != null && bidVendorFilterDTO.getBidType() != null) {
                    BidSpecialistScoreFilterDTO bidSpecialistScoreFilterDTO = new BidSpecialistScoreFilterDTO();
                    bidSpecialistScoreFilterDTO.setBidSheetId(bidVendorFilterDTO.getBidSheetId());
                    bidSpecialistScoreFilterDTO.setUserId(bidVendorFilterDTO.getSpecialistUserId());
                    bidSpecialistScoreFilterDTO.setBidVendorId(bidVendorVO.getId());
                    bidSpecialistScoreFilterDTO.setBidType(bidVendorFilterDTO.getBidType());
                    bidSpecialistScoreFilterDTO.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
                    List<BidSpecialistScoreVO> bidSpecialistScoreVOS = bidSpecialistScoreService.queryBidSpecialistScoreVOList(bidSpecialistScoreFilterDTO);
                    if (CollectionUtil.isNotEmpty(bidSpecialistScoreVOS)) {
                        bidVendorVO.setBidSpecialistScoreVO(bidSpecialistScoreVOS.get(NumberConstant.ZERO));
                    }
                }
//                bidVendorVO.setTotalPrice(bidVendorVO.getTotalPrice()==null? 0:bidVendorVO.getTotalPrice());
//                bidVendorVO.setTotalScore(bidVendorVO.getTotalScore()==null? 0:bidVendorVO.getTotalScore());

                bidVendorVO.setVendorVO(vendorService.showVendorById(bidVendorVO.getVendorId()));
            });

            BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidVendorFilterDTO.getBidSheetId());
            if(ZtbConstant.BEST_QUOTATION.equals(bidSheetVO.getRankingRules())){
                bidVendorVOS = bidVendorVOS.stream().sorted(
                        Comparator.comparing(BidVendorVO::getTotalPrice,Comparator.nullsFirst(Double::compareTo)).reversed())
                        .collect(Collectors.toList());
            }else if(ZtbConstant.BEST_SCORE.equals(bidSheetVO.getRankingRules())){
                bidVendorVOS = bidVendorVOS.stream().sorted(
                        Comparator.comparing(BidVendorVO::getTotalScore,Comparator.nullsFirst(Double::compareTo)).reversed())
                        .collect(Collectors.toList());
            }

            pageVO.setRecords(bidVendorVOS);
        }

        return pageVO;
    }

    @Override
    public PageVO<BidVendorVO> waiveAuth(Long bidSheetId) {
        BidVendorFilterDTO bidVendorFilterDTO=new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(bidSheetId);
        List<BidVendorVO> bidVendorVOS = queryBidVendorVOList(bidVendorFilterDTO);
        if(CollectionUtil.isEmpty(bidVendorVOS)){
            return null;
        }
        List<BidVendorDTO> bidVendorDTOList=CollectionUtil.newArrayList();
        bidVendorVOS.forEach(bidVendorVO -> {
            BidVendorDTO bidVendorDTO = BeanUtil.copyProperties(bidVendorVO,BidVendorDTO.class);
            bidVendorDTO.setVendorStatus(ZtbConstant.UNAUTHORIZED);
            bidVendorDTO.setConfirmedPrice(null);
            bidVendorDTOList.add(bidVendorDTO);
        });
        saveOrUpdateBatchByDTOList(bidVendorDTOList);

        bidVendorFilterDTO.setCurrentPage(NumberConstant.A_NEGATIVE);

        return queryBidVendorWithVendor(bidVendorFilterDTO);
    }

    /**
     * 重置投标供应商
     * 更新供应商重开RFQ状态,轮次,查看RFQ时间清空
     *
     * @param bidVendorId 投标供应商id
     * @param againStatus 再一次状态
     */
    private BidVendor resetBidVendor(long bidVendorId, String againStatus) {
        //更新供应商重开RFQ状态,轮次,查看RFQ时间清空
        BidVendor bidVendor = bidVendorMapper.selectById(bidVendorId);
        bidVendor.setAgainStatus(againStatus);

        if (ZtbConstant.INVITED.equals(againStatus)) {
            bidVendor.setVendorStatus(ZtbConstant.VENDOR_NO_REPLIED);
            bidVendor.setRound(bidVendor.getRound() + 1);
            bidVendor.setCheckTime(null);
            bidVendor.setReplyTime(null);
            bidVendor.setQualificationView(NumberConstant.ZERO);
            bidVendor.setTechnologyView(NumberConstant.ZERO);
            bidVendor.setCommerceView(NumberConstant.ZERO);
            bidVendor.setQualificationScore(Double.valueOf(NumberConstant.ZERO));
            bidVendor.setTechnologyScore(Double.valueOf(NumberConstant.ZERO));
            bidVendor.setCommerceScore(Double.valueOf(NumberConstant.ZERO));
            bidVendor.setTotalScore(Double.valueOf(NumberConstant.ZERO));
//            bidVendor.setTotalPrice(Double.valueOf(NumberConstant.ZERO));
            bidVendor.setConfirmedPrice(null);
        }

        boolean result = updateById(bidVendor);
        if (!result) {
            log.error("returnBidVendor() The BidVendor update faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_UPDATE_FAIL);
        }

        return bidVendor;
    }

    /**
     * 重置投标供应商的细节
     *
     * @param bidSheetId 报价表的id
     */
    private void resetBidVendorDetail(long bidSheetId, BidVendor bidVendor) {
        //受邀，更新提交状态的明细改为历史，并新增保存状态的明细,轮次+1
        //修改原评分状态为历史,并新增最新状态的专家评分
        BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
        bidVendorDetailFilterDTO.setBidVendorId(bidVendor.getId());
        bidVendorDetailFilterDTO.setStatus(ZtbConstant.SUBMIT);
        List<BidVendorDetailVO> bidVendorDetailVOS = bidVendorDetailService.queryBidVendorDetailVOList(bidVendorDetailFilterDTO);
        if (CollectionUtil.isNotEmpty(bidVendorDetailVOS)) {

            List<BidVendorDetailDTO> bidVendorDetailDTOS = CollectionUtil.newArrayList();
            bidVendorDetailVOS.forEach(bidVendorDetailVO -> {
                BidVendorDetailDTO bidVendorDetailDTO = BeanUtil.copyProperties(bidVendorDetailVO, BidVendorDetailDTO.class);
                bidVendorDetailDTO.setStatus(ZtbConstant.HISTORY);
                bidVendorDetailDTOS.add(bidVendorDetailDTO);
            });
            bidVendorDetailService.saveOrUpdateBatchByDTOList(bidVendorDetailDTOS);
        }

//        if (CollectionUtil.isEmpty(bidVendorDetailVOS)) {
//            log.error("returnBidVendor() The BidVendorDetail select faild");
//            throw new ZtbWebException(ResponseEnum.BID_VENDOR_DETAIL_SELECT_FAIL);
//        }

        //新增保存状态的明细,轮次+1
        //查询不到提交明细后，查询最新明细
        if (CollectionUtil.isEmpty(bidVendorDetailVOS)) {
            BidVendorDetailFilterDTO bidVendorDetailFilterDTO1 = new BidVendorDetailFilterDTO();
            bidVendorDetailFilterDTO1.setBidVendorId(bidVendor.getId());
            bidVendorDetailFilterDTO1.setBidSheetId(bidSheetId);
            bidVendorDetailVOS = bidVendorDetailService.queryNewBidVendorDetail(bidVendorDetailFilterDTO1);
        }

        List<BidVendorDetail> bidVendorDetails = CollectionUtil.newArrayList();
        bidVendorDetailVOS.forEach(bidVendorDetailVO -> {
            BidVendorDetail bidVendorDetail = BeanUtil.copyProperties(bidVendorDetailVO, BidVendorDetail.class);
            bidVendorDetail.setId(null);
            bidVendorDetail.setRound(bidVendor.getRound());
            bidVendorDetail.setStatus(ZtbConstant.BID_SAVE_STATUS);
            bidVendorDetail.setUpdatedTime(null);
            bidVendorDetails.add(bidVendorDetail);
        });

//        //查询出招标明细
//        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
//        bidDetailFilterDTO.setBidSheetId(bidSheetId);
//        List<BidDetailVO> bidDetailVOS = bidDetailService.queryBidDetailVOList(bidDetailFilterDTO);
//        if (CollectionUtil.isEmpty(bidDetailVOS)) {
//            log.error("approveBeforeJudgeStatus() BidDetailVO select faild");
//            throw new ZtbWebException(ResponseEnum.BID_DETAIL_SELECT_ERROR);
//        }
//
//        List<BidVendorDetail> bidVendorDetails = CollectionUtil.newArrayList();
//        bidDetailVOS.forEach(bidDetailVO -> {
//            BidVendorDetail bidVendorDetail = BeanUtil.copyProperties(bidDetailVO, BidVendorDetail.class);
//            bidVendorDetail.setId(null);
//            bidVendorDetail.setRound(bidVendor.getRound());
//            bidVendorDetail.setStatus(ZtbConstant.BID_SAVE_STATUS);
//            bidVendorDetail.setBidVendorId(bidVendor.getId());
//            bidVendorDetails.add(bidVendorDetail);
//        });

        bidVendorDetailService.saveOrUpdateBatch(bidVendorDetails);
    }

    /**
     * 重置收购专家评分
     *
     */
    private void resetBidSpecialistScore(BidVendor bidVendor) {
        //修改原评分状态为历史,并新增最新状态的专家评分
        BidSpecialistScoreFilterDTO bidSpecialistScoreFilterDTO = new BidSpecialistScoreFilterDTO();
        bidSpecialistScoreFilterDTO.setBidVendorId(bidVendor.getId());
        bidSpecialistScoreFilterDTO.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
        List<BidSpecialistScoreVO> bidSpecialistScoreVOS = bidSpecialistScoreService.queryBidSpecialistScoreVOList(bidSpecialistScoreFilterDTO);
        List<BidSpecialistScoreDTO> bidSpecialistScoreDTOS = CollectionUtil.newArrayList();
        bidSpecialistScoreVOS.forEach(bidSpecialistScoreVO -> {
            bidSpecialistScoreVO.setStatus(ZtbConstant.HISTORY);
            BidSpecialistScoreDTO bidSpecialistScoreDTO =
                    BeanUtil.copyProperties(bidSpecialistScoreVO, BidSpecialistScoreDTO.class);
            bidSpecialistScoreDTOS.add(bidSpecialistScoreDTO);
        });
        bidSpecialistScoreService.saveOrUpdateBatchByDTOList(bidSpecialistScoreDTOS);
        //新增最新状态的专家评分
        List<BidSpecialistScore> bidSpecialistScores = CollectionUtil.newArrayList();
        bidSpecialistScoreVOS.forEach(bidSpecialistScoreVO -> {
            BidSpecialistScore bidSpecialistScore = BeanUtil.copyProperties(bidSpecialistScoreVO, BidSpecialistScore.class);
            bidSpecialistScore.setId(null);
            bidSpecialistScore.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
            bidSpecialistScore.setWhetherView(NumberConstant.ZERO);
            bidSpecialistScore.setViewTime(null);
            bidSpecialistScore.setScore(null);
            bidSpecialistScore.setComment(null);
            bidSpecialistScore.setRound(bidVendor.getRound());
            bidSpecialistScore.setUpdatedTime(null);
            bidSpecialistScore.setUpdatedUser(null);
            bidSpecialistScore.setCommentTime(null);
            bidSpecialistScores.add(bidSpecialistScore);
        });

//        List<BidSpecialistScore> bidSpecialistScores = CollectionUtil.newArrayList();
//        BidVendorVO bidVendorVO = queryBidVendorById(bidVendorId);
//        bidSpecialistScores = bidSheetService.insertBidSpecialistScore(bidSpecialistVOS, bidSheetVO,
//                bidVendorId, bidVendorVO.getVendorName(), bidSpecialistScores);

        bidSpecialistScoreService.saveOrUpdateBatch(bidSpecialistScores);
    }
}
