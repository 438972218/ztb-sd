package com.xdcplus.vendor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.pojo.dto.BidVendorDTO;
import com.xdcplus.vendor.common.pojo.dto.BidVendorFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.BidSheetVO;
import com.xdcplus.vendor.generator.impl.BidVendorBaseServiceImpl;
import com.xdcplus.vendor.mapper.BidVendorMapper;
import com.xdcplus.vendor.common.pojo.entity.BidVendor;
import com.xdcplus.vendor.common.pojo.vo.BidVendorVO;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.BidSheetService;
import com.xdcplus.vendor.service.BidVendorDetailService;
import com.xdcplus.vendor.service.BidVendorService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 招标投标方(BidVendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:42:09
 */
@Slf4j
@Service("bidVendorService")
public class BidVendorServiceImpl extends BidVendorBaseServiceImpl<BidVendor, BidVendorVO, BidVendor, BidVendorMapper> implements BidVendorService {

    @Autowired
    BidVendorDetailService bidVendorDetailService;

    @Autowired
    PermService permService;

    @Autowired
    BidSheetService bidSheetService;

    @Override
    public void updateBidVendorStatusByVendorUserId(BidVendorDTO bidVendorDTO) {
        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidVendorDTO.getBidSheetId());
        if(!StrUtil.equals(bidSheetVO.getRequestStatusName(), ZtbConstant.RUNNING)){
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_REPLY_ERROR);
        }

        List<BidVendor> bidVendors = bidVendorMapper.selectList(new QueryWrapper<BidVendor>().lambda().eq(BidVendor::getBidSheetId,bidVendorDTO.getBidSheetId())
                .eq(BidVendor::getVendorUserId,bidVendorDTO.getVendorUserId()));
        if(CollectionUtil.isEmpty(bidVendors)){
            log.error("updateBidVendorStatusByVendorUserId() bidVendors select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_SELECT_FAIL);
        }
        BidVendor bidVendor =bidVendors.get(NumberConstant.ZERO);
        bidVendor.setVendorStatus(bidVendorDTO.getVendorStatus());
        int result = bidVendorMapper.updateById(bidVendor);
        if (result == 0) {
            log.error("updateBidVendorStatusByVendorUserId() bidVendors update faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_UPDATE_FAIL);
        }
        bidVendorDetailService.updateBidVendorDetailByOperation(bidVendor.getId(),bidVendorDTO.getVendorStatus());
    }

    @Override
    public void updateBidVendorCheckTimeByVendorUserId(BidVendorDTO bidVendorDTO) {
        List<BidVendor> bidVendors = bidVendorMapper.selectList(new QueryWrapper<BidVendor>().lambda()
                .eq(BidVendor::getBidSheetId,bidVendorDTO.getBidSheetId())
                .eq(BidVendor::getVendorUserId,bidVendorDTO.getVendorUserId()));
        if(CollectionUtil.isEmpty(bidVendors)){
            log.error("updateBidVendorCheckTimeByVendorUserId() The BidVendor select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_SELECT_FAIL);
        }
        BidVendor bidVendor=bidVendors.get(NumberConstant.ZERO);
        if(bidVendor.getCheckTime()==null){
            bidVendor.setCheckTime(DateUtil.current());
            bidVendorMapper.updateById(bidVendor);
        }
    }

    @Override
    public PageVO<BidVendorVO> queryBidVendorWithUser(BidVendorFilterDTO bidVendorFilterDTO) {
        bidVendorFilterDTO.setDeleted(0);
        PageVO<BidVendorVO> pageVO = new PageVO<>();

        if (bidVendorFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidVendorFilterDTO.getCurrentPage(), bidVendorFilterDTO.getPageSize(),
                    bidVendorFilterDTO.getOrderType(), bidVendorFilterDTO.getOrderField());
        }

        List<BidVendor> bidVendorList = queryBidVendorList(bidVendorFilterDTO);

        PageInfo<BidVendor> pageInfo = new PageInfo<>(bidVendorList);
        List<BidVendorVO> bidVendorVOS = this.objectConversion(bidVendorList);
        if(CollectionUtil.isEmpty(bidVendorList)){
            return null;
        }
        bidVendorVOS.forEach(bidVendorVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidVendorVO.getVendorUserId());
            bidVendorVO.setSysUserInfoVO(sysUserInfoVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, bidVendorVOS);

        return pageVO;
    }
}
