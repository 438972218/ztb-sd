package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.pojo.bo.BidItemsBO;
import com.xdcplus.vendor.common.pojo.dto.OfferDTO;
import com.xdcplus.vendor.common.pojo.dto.OfferFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.Offer;
import com.xdcplus.vendor.common.pojo.query.OfferQuery;
import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import com.xdcplus.vendor.common.pojo.vo.PaidVendorVO;
import com.xdcplus.vendor.common.pojo.vo.VendorUserVO;
import com.xdcplus.vendor.common.pojo.vo.VendorVO;
import com.xdcplus.vendor.mapper.OfferMapper;
import com.xdcplus.vendor.remote.service.FeignService;
import com.xdcplus.vendor.service.OfferService;
import com.xdcplus.vendor.service.PaidVendorService;
import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.vendor.service.VendorUserService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 出价记录 服务实现类
 *
 * @author Rong.Jia
 * @since 2021-08-17
 */
@Slf4j
@Service
public class OfferServiceImpl extends BaseServiceImpl<Offer, OfferVO, Offer, OfferMapper> implements OfferService {

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private FeignService feignService;

    @Autowired
    private VendorUserService vendorUserService;

    @Autowired
    private PaidVendorService paidVendorService;

    @Autowired
    private VendorService vendorService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public OfferVO saveOffer(OfferDTO offerDTO) {
        return saveOffer(offerDTO, NumberConstant.ZERO.longValue());
    }

    @Override
    public OfferVO findOne(Long offerId) {

        Assert.notNull(offerId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return this.objectConversion(this.getById(offerId));
    }

    @Override
    public Integer getRanking(Long offerId, Boolean positive) {

        Assert.notNull(offerId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        Offer offer = this.getById(offerId);
        if (ObjectUtil.isNotNull(offer)) {
            if (Validator.equal(NumberConstant.ZERO.byteValue(), offer.getAvailable())) {
                offer = offerMapper.findLastOfferByOfferUser(offer.getRequestId(), offer.getOfferGoods(), offer.getOfferUser(), NumberConstant.ONE.byteValue());
            }
            if (ObjectUtil.isNotNull(offer)) {
                List<OfferVO> offerVOList = this.groupOfferByRequestId(offer.getRequestId(), positive, NumberConstant.ONE.byteValue());
                if (CollectionUtil.isNotEmpty(offerVOList)) {
                    List<PriceTimeCorrelation> priceTimeCorrelationList = priceTimeCorrelationSorting(getPriceTimeCorrelation(offerVOList), positive);
                    for (int i = 1; i <= priceTimeCorrelationList.size(); i++) {
                        if (Validator.equal(offerId, priceTimeCorrelationList.get(i -1).getUnique())) {
                            return i;
                        }
                    }
                }
            }
        }
        return NumberConstant.ZERO;
    }

    @Override
    public List<OfferVO> findOfferByOfferGoods(Long requestId, String offerGoods, Boolean positive) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<OfferVO> offerVOList = this.objectConversion(offerMapper.findOfferByOfferGoods(requestId, offerGoods));
        if (CollectionUtil.isNotEmpty(offerVOList)) {
            this.computingRankings(offerVOList, positive);
        }

        return offerVOList;
    }

    @Override
    public PageVO<OfferVO> findOffer(OfferFilterDTO filterDTO) {

        PageVO<OfferVO> pageVO = new PageVO<>();

        if (filterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(filterDTO);
        }

        OfferQuery offerQuery = BeanUtil.copyProperties(filterDTO, OfferQuery.class);
        offerQuery.setParentId(NumberConstant.ZERO.longValue());
        List<Offer> offerList = offerMapper.findOffer(offerQuery);
        PageInfo<Offer> pageInfo = new PageInfo<>(offerList);
        List<OfferVO> offerVOList = this.objectConversion(pageInfo.getList());
        if (CollectionUtil.isEmpty(offerVOList)) {
            return null;
        }
        offerVOList.forEach(offerVO -> {
            VendorVO vendorVO = vendorService.queryVendorById(offerVO.getVendorId());
            if (ObjectUtil.isNotEmpty(vendorVO)) {
                offerVO.setVendorName(vendorVO.getName());
            }
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, offerVOList);

        return pageVO;
    }

    @Override
    public void getRanking(OfferVO offerVO, Boolean positive) {

        offerVO.setRanking(getRanking(offerVO.getId(), positive));
        if (CollectionUtil.isEmpty(offerVO.getChildren())) {
            return;
        }

        for (OfferVO child : offerVO.getChildren()) {
            getRanking(child, positive);
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateAvailableById(Long id, Byte available) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        Offer offer = this.getById(id);

        if (ObjectUtil.isNotNull(offer)) {
            offerMapper.updateAvailableById(id, available);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateTriggerThresholdById(Long id, Byte triggerThreshold) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        Offer offer = this.getById(id);

        if (ObjectUtil.isNotNull(offer)) {
            offerMapper.updateTriggerThresholdById(id, triggerThreshold);
        }
    }

    @Override
    public List<OfferVO> findOffer(Long requestId, String offerGoods, Byte available, String offerUser) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        return this.objectConversion(offerMapper.findOffers(requestId, offerGoods, available, offerUser));
    }

    @Override
    public List<OfferVO> getLatestRanking(Long requestId, String offerGoods, Boolean positive) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<Offer> offerList = positive ? offerMapper.latestMaxGroupOffer(requestId, offerGoods, null)
                : offerMapper.latestMinGroupOffer(requestId, offerGoods, null);

        if (CollectionUtil.isNotEmpty(offerList)) {
            List<OfferVO> offerVOList = new ArrayList<>();
            for (Offer offer : offerList) {
                OfferVO offerVO = super.objectConversion(offer);
                this.objectConversion(offerVO, offer.getId(), Boolean.TRUE, positive);
                offerVOList.add(offerVO);
            }

            return offerVOList;
        }

        return null;
    }

    @Override
    public List<OfferVO> groupOfferByRequestId(Long requestId, Boolean positive) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        return this.groupOfferByRequestId(requestId, positive, null);
    }

    @Override
    public List<OfferVO> groupOfferByRequestId(Long requestId, Boolean positive, Byte available) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<Offer> offerList = positive ? offerMapper.groupMaxOfferByRequestId(requestId, available, Boolean.TRUE)
                : offerMapper.groupMinOfferByRequestId(requestId, available, Boolean.TRUE);
        return this.objectConversion(offerList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteOfferByRequestId(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        int result = offerMapper.delete(new QueryWrapper<Offer>().lambda().eq(Offer::getRequestId, requestId));

        return result >= 1;
    }

    @Override
    public Boolean existenceSubidentityByRequestId(Long requestId, Byte available) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        Set<Long> parentIds = offerMapper.groupOfferParentIdsByRequestId(requestId, available);
        return parentIds.stream().anyMatch(a -> !Validator.equal(NumberConstant.ZERO.longValue(), a));

    }

    @Override
    public List<OfferVO> computingRankings(List<OfferVO> offerVOList, Boolean direction) {

        if (CollectionUtil.isNotEmpty(offerVOList)) {

            List<PriceTimeCorrelation> priceTimeCorrelationList = priceTimeCorrelationSorting(getPriceTimeCorrelation(offerVOList), direction);

            for (int i = 1; i <= priceTimeCorrelationList.size(); i++) {
                PriceTimeCorrelation priceTimeCorrelation = priceTimeCorrelationList.get(i - 1);
                for (OfferVO offerVO : offerVOList) {
                    if (Validator.equal(priceTimeCorrelation.getUnique(), offerVO.getId())) {
                        offerVO.setRanking(i);
                    }
                }
            }
        }

        return offerVOList.stream().sorted(Comparator.comparing(OfferVO::getRanking)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Set<String>> findCategoryBiddingItems(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<BidItemsBO> categoryBiddingItems = offerMapper.findCategoryBiddingItems(requestId, NumberConstant.ONE.byteValue());
        if (CollectionUtil.isNotEmpty(categoryBiddingItems)) {
            Map<String, Set<String>> results = MapUtil.newHashMap();
            for (BidItemsBO categoryBiddingItem : categoryBiddingItems) {
                String offerUser = categoryBiddingItem.getOfferUser();
                if (results.containsKey(offerUser)) {
                    Set<String> offerGoods = results.get(offerUser);
                    offerGoods.add(categoryBiddingItem.getOfferGoods());
                    results.put(offerUser, offerGoods);
                } else {
                    results.put(offerUser, CollectionUtil.newHashSet(categoryBiddingItem.getOfferGoods()));
                }
            }
            return results;
        }

        return null;
    }

    @Override
    public OfferVO objectConversion(Offer offer) {
        OfferVO offerVO = super.objectConversion(offer);
        if (ObjectUtil.isNotNull(offerVO)) {
            PaidVendorVO paidVendorVO = paidVendorService.findPaidVendorVOByUserNameAndRequestId(offer.getOfferUser(), offer.getRequestId());
            Optional.ofNullable(paidVendorVO).ifPresent(a -> offerVO.setVendorMark(a.getVendorMark()));
            objectConversion(offerVO, offer.getId(), Boolean.FALSE, Boolean.FALSE);
        }

        return offerVO;

    }

    /**
     * 获取价格时间关系
     *
     * @param offerVOList 出价记录集合
     * @return {@link List}<{@link PriceTimeCorrelation}>
     */
    private List<PriceTimeCorrelation> getPriceTimeCorrelation(List<OfferVO> offerVOList) {
        return offerVOList.stream()
                .map(a -> PriceTimeCorrelation.builder().money(Convert.toDouble(a.getMoney())).time(a.getOfferTime()).unique(a.getId()).build())
                .collect(Collectors.toList());
    }

    /**
     * 价格时间相关性排序
     *
     * @param priceTimeCorrelations 价格的时间相关性
     * @param direction             方向 true: 正向。false: 反向
     * @return {@link List}<{@link PriceTimeCorrelation}>
     */
    private List<PriceTimeCorrelation> priceTimeCorrelationSorting(List<PriceTimeCorrelation> priceTimeCorrelations, Boolean direction) {
        return direction ? priceTimeCorrelations.stream().sorted(Comparator.comparing(PriceTimeCorrelation::getMoney)
                .reversed().thenComparing(PriceTimeCorrelation::getTime)).collect(Collectors.toList())
                : priceTimeCorrelations.stream().sorted(Comparator.comparing(PriceTimeCorrelation::getMoney)
                .thenComparing(PriceTimeCorrelation::getTime)).collect(Collectors.toList());
    }

    /**
     * 保存出价记录
     *
     * @param parentId 父id
     * @param offerDTO 出价记录
     * @return {@link OfferVO} 出价记录
     */
    private OfferVO saveOffer(OfferDTO offerDTO, Long parentId) {

        if (Validator.isNull(parentId)) {
            parentId = NumberConstant.ZERO.longValue();
        }

        Offer offer = new Offer();
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreNullValue(Boolean.TRUE);
        BeanUtil.copyProperties(offerDTO, offer, copyOptions);
        offer.setCreatedTime(DateUtil.current());
        offer.setParentId(parentId);
        offer.setOfferTime(DateUtil.current());

        SysUserInfoVO sysUserInfoVO = feignService.findUserInfoByUserName(offerDTO.getOfferUser());
        if (ObjectUtil.isNotNull(sysUserInfoVO)) {
            VendorUserVO vendorUserVO = vendorUserService.findVendorUserByUserId(sysUserInfoVO.getId());
            if (ObjectUtil.isNotNull(vendorUserVO)) {
                offer.setVendorId(vendorUserVO.getVendorId());
            }
        }

        this.save(offer);

        List<OfferDTO> children = offerDTO.getChildren();
        if (CollectionUtil.isEmpty(children)) {
            return this.objectConversion(offer);
        }

        for (OfferDTO child : children) {
            saveOffer(child, offer.getId());
        }

        return this.objectConversion(offer);
    }

    /**
     * 对象转换
     *
     * @param offerVO   出价记录
     * @param parentId  父id
     * @param isRanking 是否排名
     * @param direction 方向， true: 正向。false: 反向， isRanking:true有效
     */
    private void objectConversion(OfferVO offerVO, Long parentId, Boolean isRanking, Boolean direction) {

        if (isRanking) {
            getRanking(offerVO, direction);
        }

        List<Offer> offerList = offerMapper.findOfferByParentId(parentId);
        if (CollectionUtil.isEmpty(offerList)) {
            return;
        }

        offerVO.setChildren(this.objectConversion(offerList));
    }

    /**
     * 价格与时间关联
     *
     * @author Rong.Jia
     * @date 2021/11/03
     */
    @Getter
    @Builder
    private static class PriceTimeCorrelation {

        /**
         *  标识
         */
        private Long unique;

        /**
         * 价格
         */
        private Double money;

        /**
         * 时间
         */
        private Long time;

    }


}
