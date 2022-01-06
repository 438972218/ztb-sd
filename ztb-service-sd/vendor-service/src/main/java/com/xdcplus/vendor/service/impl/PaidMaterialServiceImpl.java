package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.enums.BidStructure;
import com.xdcplus.vendor.common.enums.DirectionOfEnum;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.*;
import com.xdcplus.vendor.generator.impl.PaidMaterialBaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidMaterialMapper;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterial;
import com.xdcplus.vendor.remote.service.IeService;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.*;
import com.xdcplus.vendor.websocket.BidRankingMessage;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 竞价品项(PaidMaterial)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:01:55
 */
@Slf4j
@Service("paidMaterialService")
public class PaidMaterialServiceImpl extends PaidMaterialBaseServiceImpl<PaidMaterial, PaidMaterialVO, PaidMaterial, PaidMaterialMapper> implements PaidMaterialService {

    @Autowired
    PaidSheetService paidSheetService;

    @Autowired
    OfferService offerService;

    @Autowired
    PermService permService;

    @Autowired
    PaidVendorService paidVendorService;

    @Autowired
    PaidMaterialMandateService paidMaterialMandateService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private IeService ieService;

    @Override
    public List<PaidMaterialVO> queryPaidMaterialVOBySheetId(Long id) {
        PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
        paidMaterialFilterDTO.setPaidSheetId(id);
        paidMaterialFilterDTO.setParentId(Long.valueOf(NumberConstant.ZERO));
        List<PaidMaterialVO> paidMaterialVOS = queryPaidMaterialVOList(paidMaterialFilterDTO);
        if (CollectionUtil.isEmpty(paidMaterialVOS)) {
            return null;
        }
        PaidSheetVO paidSheetVO = paidSheetService.queryPaidSheetById(id);
        if (ZtbConstant.TOTAL_SUM.equals(paidSheetVO.getBidStructure())) {
            paidMaterialVOS.forEach(paidMaterialVO -> {
                PaidMaterialFilterDTO childPaidMaterialFilterDTO = new PaidMaterialFilterDTO();
                childPaidMaterialFilterDTO.setParentId(paidMaterialVO.getId());
                List<PaidMaterialVO> childPaidMaterialVOS = queryPaidMaterialVOList(childPaidMaterialFilterDTO);
                if (CollectionUtil.isNotEmpty(childPaidMaterialVOS)) {
                    paidMaterialVO.setChildPaidMaterialVOS(childPaidMaterialVOS);
                }
            });
        }

        return paidMaterialVOS;
    }

    @Override
    public List<PaidMaterialVO> queryPaidMaterialVOByRequestId(Long id) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(id);
        List<PaidSheetVO> paidSheetVOS = paidSheetService.queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            log.error("queryPaidMaterialVOByRequestId() paidSheetVOS select faild");
            throw new ZtbWebException(ResponseEnum.PAID_MATERIAL_SELECT_FAIL);
        }

        PaidSheetVO paidSheetVO = paidSheetVOS.get(NumberConstant.ZERO);
        PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
        paidMaterialFilterDTO.setPaidSheetId(paidSheetVO.getId());
        paidMaterialFilterDTO.setParentId(Long.valueOf(NumberConstant.ZERO));
        List<PaidMaterialVO> paidMaterialVOS = queryPaidMaterialVOList(paidMaterialFilterDTO);
        if (CollectionUtil.isEmpty(paidMaterialVOS)) {
            return null;
        }

        return paidMaterialVOS;
    }

    @Override
    public List<PaidMaterialVO> getLatestRankingByRequestId(Long requestId) {
        PaidSheetVO paidSheetVO = paidSheetService.findPaidSheetByRequestId(requestId);
        List<PaidMaterialVO> paidMaterialVOS = queryPaidMaterialVOBySheetId(paidSheetVO.getId());

        boolean paidDirection = DirectionOfEnum.POSITIVE.getValue().equals(paidSheetVO.getPaidDirection()) ? Boolean.TRUE : Boolean.FALSE;
        for (PaidMaterialVO paidMaterialVO : paidMaterialVOS) {
            List<OfferVO> offerVOS = offerService.getLatestRanking(requestId, String.valueOf(paidMaterialVO.getId()), paidDirection);
            paidMaterialVO.setOfferVOS(offerVOS);
        }

        return paidMaterialVOS;
    }

    @Override
    public BidRankingMessage getRankingByVendorUserIdAndRequestId(Long vendorUserId, Long requestId) {

        BidRankingMessage bidRankingMessage = getRankingByRequestId(requestId);
        if(ObjectUtil.isEmpty(bidRankingMessage)){
            return null;
        }
        Map<String, List<BidRankingMessage.BidRanking>> singleRanking = bidRankingMessage.getSingleRanking();
        List<BidRankingMessage.BidRanking> totalRanking = bidRankingMessage.getTotalRanking();


        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorUserId);
        if (ObjectUtil.isEmpty(sysUserInfoVO)) {
            return null;
        }
        String vendorUserName = sysUserInfoVO.getUserName();

        if (CollectionUtil.isNotEmpty(singleRanking)) {

            BidRankingMessage.BidRankingMessageBuilder builder = BidRankingMessage.builder().requestId(requestId);

            if(CollectionUtil.isNotEmpty(totalRanking)){
                Map<String, List<BidRankingMessage.BidRanking>> bidRankingMap = totalRanking.stream().collect(Collectors.groupingBy(BidRankingMessage.BidRanking::getOfferUser));

                if (bidRankingMap.containsKey(vendorUserName)) {
                    builder.totalRanking(bidRankingMap.get(vendorUserName));
                }
            }

            Map<String, List<BidRankingMessage.BidRanking>> singleRankingMap = MapUtil.newHashMap();
            for (Map.Entry<String, List<BidRankingMessage.BidRanking>> entry : singleRanking.entrySet()) {
                Map<String, List<BidRankingMessage.BidRanking>> tempMap = entry.getValue().stream()
                        .collect(Collectors.groupingBy(BidRankingMessage.BidRanking::getOfferUser));
                if (tempMap.containsKey(vendorUserName)) {
                    singleRankingMap.put(entry.getKey(), tempMap.get(vendorUserName));
                }
            }
            builder.singleRanking(singleRankingMap);

            return builder.build();
        } else {
            return null;
        }
    }

    @Override
    public BidRankingMessage getRankingByRequestId(Long requestId) {
        PaidSheetVO paidSheetVO = paidSheetService.findPaidSheetByRequestId(requestId);
        if (ObjectUtil.isEmpty(paidSheetVO)) {
            return null;
        }
        boolean direction = DirectionOfEnum.POSITIVE.getValue().equals(paidSheetVO.getPaidDirection()) ? Boolean.TRUE : Boolean.FALSE;

        List<OfferVO> offerVOList = offerService.groupOfferByRequestId(requestId, direction,NumberConstant.ONE.byteValue());
        if (CollectionUtil.isEmpty(offerVOList)) {
            return null;
        }

        Integer paidMaterialCount = paidMaterialMapper.selectCount(
                new QueryWrapper<PaidMaterial>().lambda()
                        .eq(PaidMaterial::getPaidSheetId,paidSheetVO.getId())
                        .eq(PaidMaterial::getParentId,NumberConstant.ZERO)
                        .eq(PaidMaterial::getDeleted,NumberConstant.ZERO));

        if (paidSheetVO.getPaidStatusMark().equals(NumberConstant.FOUR)) {
            Map<Long, List<PaidVendorVO>> paidVendorMap = paidVendorService.findPaidVendorVOMapByRequestId(requestId);
            offerVOList.forEach(offerVO -> {
                List<PaidVendorVO> paidVendorVOS = paidVendorMap.get(offerVO.getVendorId());
                if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
                    offerVO.setVendorName(paidVendorVOS.get(NumberConstant.ZERO).getVendorName());
                }
            });
        }

        // key: 出价人，value: 出价物品
        Map<String, Set<String>> categoryBiddingItems = offerService.findCategoryBiddingItems(requestId);

        List<BidRankingMessage.BidRanking> totalRanking = null;

        // key: 物品标识，value: 排名
        Map<String, List<BidRankingMessage.BidRanking>> singleRanking = null;

        // 是否是多物品竞价，true: 是，false: 否
        Boolean moreItems = BidStructure.MORE_ITEMS.getValue().equals(paidSheetVO.getBidStructure()) ? Boolean.TRUE : Boolean.FALSE;
        if (moreItems) {

            Map<String, OfferVO> offerUserMap = offerVOList.stream().collect(Collectors.groupingBy(OfferVO::getOfferUser))
                    .entrySet().stream()
                    .filter(x -> categoryBiddingItems.containsKey(x.getKey())
                            && categoryBiddingItems.get(x.getKey()).size() == paidMaterialCount
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, a -> {
                        Double totalValue = a.getValue().stream()
                                .map(b -> Convert.toDouble(b.getMoney()))
                                .mapToDouble(Double::doubleValue).sum();
                        OfferVO offerVO = BeanUtil.copyProperties(a.getValue().get(NumberConstant.ZERO), OfferVO.class);
                        a.getValue().stream().max(Comparator.comparing(OfferVO::getOfferTime)).ifPresent(c -> BeanUtil.copyProperties(c, offerVO));
                        offerVO.setMoney(Convert.toStr(totalValue));
                        return offerVO;
                    }));

            singleRanking = getSingleRanking(offerVOList, direction);
            totalRanking = getTotalRanking(CollectionUtil.newArrayList(offerUserMap.values()), direction);
        } else {

            singleRanking = getSingleRanking(offerVOList, direction);
            totalRanking = getTotalRanking(offerVOList, direction);
        }


        BidRankingMessage bidRankingMessage = BidRankingMessage.builder()
                .singleRanking(singleRanking)
                .totalRanking(totalRanking)
                .requestId(requestId)
                .build();
        return bidRankingMessage;
    }

    @Override
    public BidRankingMessage getRankingByRequestIdAndMaterialId(Long requestId, Long materialId) {
        PaidSheetVO paidSheetVO = paidSheetService.findPaidSheetByRequestId(requestId);
        if (ObjectUtil.isEmpty(paidSheetVO)) {
            return null;
        }
        boolean direction = DirectionOfEnum.POSITIVE.getValue().equals(paidSheetVO.getPaidDirection()) ? Boolean.TRUE : Boolean.FALSE;

        List<OfferVO> offerVOList = offerService.groupOfferByRequestId(requestId, direction,NumberConstant.ONE.byteValue());
        if (CollectionUtil.isEmpty(offerVOList)) {
            return null;
        }
        if (paidSheetVO.getPaidStatusMark().equals(NumberConstant.FOUR)) {
            Map<Long, List<PaidVendorVO>> paidVendorMap = paidVendorService.findPaidVendorVOMapByRequestId(requestId);
            offerVOList.forEach(offerVO -> {
                List<PaidVendorVO> paidVendorVOS = paidVendorMap.get(offerVO.getVendorId());
                if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
                    offerVO.setVendorName(paidVendorVOS.get(NumberConstant.ZERO).getVendorName());
                }
            });
        }

        // key: 物品标识，value: 排名
        Map<String, List<BidRankingMessage.BidRanking>> singleRanking = null;

        singleRanking = getSingleRanking(offerVOList, direction);

        List<BidRankingMessage.BidRanking> bidRankings = singleRanking.get(String.valueOf(materialId));

        BidRankingMessage bidRankingMessage = BidRankingMessage.builder()
                .totalRanking(bidRankings)
                .requestId(requestId)
                .build();

        return bidRankingMessage;
    }

    @Override
    public List<PaidMaterialVO> getMandateByRequestIdAndVendorUserId(Long requestId, Long vendorUserId) {
        List<PaidMaterialVO> paidMaterialVOS = queryPaidMaterialVOByRequestId(requestId);
        if (CollectionUtil.isEmpty(paidMaterialVOS)) {
            return null;
        }
        paidMaterialVOS.forEach(paidMaterialVO -> {
            PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO = new PaidMaterialMandateFilterDTO();
            paidMaterialMandateFilterDTO.setPaidVendorUserId(vendorUserId);
            paidMaterialMandateFilterDTO.setPaidMaterialId(paidMaterialVO.getId());
            List<PaidMaterialMandateVO> paidMaterialMandateVOS = paidMaterialMandateService.queryPaidMaterialMandateVOList(paidMaterialMandateFilterDTO);
            if (CollectionUtil.isNotEmpty(paidMaterialMandateVOS)) {
                paidMaterialVO.setPaidMaterialMandateVO(paidMaterialMandateVOS.get(NumberConstant.ZERO));
            }
        });

        return paidMaterialVOS;
    }

    @Override
    public void exportExcelpaidTotalOffer(HttpServletResponse response, Long requestId) {
        BidRankingMessage bidRankingMessage = getRankingByRequestId(requestId);
        List<BidRankingMessage.BidRanking> bidRankings = bidRankingMessage.getTotalRanking();
        RequestVO requestVO = ieService.findRequestById(requestId);

        List<BidTotalRankingExcelVO> bidTotalRankingExcelVOS =CollectionUtil.newArrayList();
        bidRankings.forEach(bidRanking -> {
            BidTotalRankingExcelVO bidTotalRankingExcelVO = BeanUtil.copyProperties(bidRanking, BidTotalRankingExcelVO.class);
            VendorVO vendorVO = vendorService.queryVendorById(bidRanking.getVendorId());
            bidTotalRankingExcelVO.setVendorName(vendorVO.getName());
            bidTotalRankingExcelVO.setOfferTime(DateUtil.format(DateUtil.date(bidRanking.getOfferTime()), DatePattern.NORM_DATETIME_FORMAT));
            bidTotalRankingExcelVOS.add(bidTotalRankingExcelVO);
        });

        try {
            ExportExcelUtil.export(response, BidTotalRankingExcelVO.class,requestVO.getOddNumber()+ZtbConstant.TOTAL_PRICING,bidTotalRankingExcelVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportExcelpaidSingleOffer(HttpServletResponse response,Long requestId) {
        BidRankingMessage bidRankingMessage = getRankingByRequestId(requestId);

        RequestVO requestVO = ieService.findRequestById(requestId);

        Map<String, List<BidRankingMessage.BidRanking>> map =bidRankingMessage.getSingleRanking();
        List<BidSingleRankingExcelVO> bidSingleRankingExcelVOS =CollectionUtil.newArrayList();
        for (Map.Entry<String, List<BidRankingMessage.BidRanking>> entry : map.entrySet()) {
            PaidMaterialVO paidMaterialVO = queryPaidMaterialById(Long.valueOf(entry.getKey()));
            List<BidRankingMessage.BidRanking> bidRankings = entry.getValue();
            if(CollectionUtil.isNotEmpty(bidRankings)){
                bidRankings.forEach(bidRanking ->{
                    BidSingleRankingExcelVO bidSingleRankingExcelVO = BeanUtil.copyProperties(bidRanking, BidSingleRankingExcelVO.class);
                    bidSingleRankingExcelVO.setOfferGoodsName(paidMaterialVO.getMaterialName());
                    VendorVO vendorVO = vendorService.queryVendorById(bidRanking.getVendorId());
                    bidSingleRankingExcelVO.setVendorName(vendorVO.getName());
                    bidSingleRankingExcelVO.setOfferTime(DateUtil.format(DateUtil.date(bidRanking.getOfferTime()), DatePattern.NORM_DATETIME_FORMAT));
                    bidSingleRankingExcelVOS.add(bidSingleRankingExcelVO);
                });
            }
        }

        try {
            ExportExcelUtil.export(response, BidSingleRankingExcelVO.class,requestVO.getOddNumber()+ZtbConstant.SINGLE_PRICING, bidSingleRankingExcelVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取单排名
     *
     * @param offers    提供了
     * @param direction 方向
     * @return {@link Map}<{@link String}, {@link List}<{@link BidRankingMessage.BidRanking}>> key: 物品标识，value: 排名
     */
    private Map<String, List<BidRankingMessage.BidRanking>> getSingleRanking(List<OfferVO> offers, Boolean direction) {

        Map<String, List<OfferVO>> offerMap = offers.stream().collect(Collectors.groupingBy(OfferVO::getOfferGoods));
        return offerMap.entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey,
                        a -> offerService.computingRankings(a.getValue(), direction).stream()
                                .map(c -> BeanUtil.copyProperties(c, BidRankingMessage.BidRanking.class))
                                .collect(Collectors.toList())));
    }

    /**
     * 获取总计排名
     *
     * @param offers    提供了
     * @param direction 方向
     * @return {@link List}<{@link BidRankingMessage.BidRanking}>
     */
    private List<BidRankingMessage.BidRanking> getTotalRanking(List<OfferVO> offers, Boolean direction) {
        return offerService.computingRankings(offers, direction).stream()
                .map(c -> BeanUtil.copyProperties(c, BidRankingMessage.BidRanking.class))
                .collect(Collectors.toList());
    }

}
