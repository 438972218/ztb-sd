package com.xdcplus.vendor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentFilterDTO;
import com.xdcplus.vendor.generator.impl.BidVendorAttachmentBaseServiceImpl;
import com.xdcplus.vendor.mapper.BidVendorAttachmentMapper;
import com.xdcplus.vendor.common.pojo.entity.BidVendorAttachment;
import com.xdcplus.vendor.common.pojo.vo.BidVendorAttachmentVO;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.BidVendorAttachmentService;
import com.xdcplus.vendor.service.BidVendorService;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 招标供应商附件(BidVendorAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:22
 */
@Slf4j
@Service("bidVendorAttachmentService")
public class BidVendorAttachmentServiceImpl extends BidVendorAttachmentBaseServiceImpl<BidVendorAttachment, BidVendorAttachmentVO, BidVendorAttachment, BidVendorAttachmentMapper> implements BidVendorAttachmentService {

    @Autowired
    PermService permService;

    @Autowired
    BidVendorService bidVendorService;

    @Override
    public PageVO<BidVendorAttachmentVO> queryBidVendorAttachmentWithName(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO) {
        bidVendorAttachmentFilterDTO.setDeleted(0);
        PageVO<BidVendorAttachmentVO> pageVO = new PageVO<>();

        if (bidVendorAttachmentFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidVendorAttachmentFilterDTO.getCurrentPage(), bidVendorAttachmentFilterDTO.getPageSize(),
                    bidVendorAttachmentFilterDTO.getOrderType(), bidVendorAttachmentFilterDTO.getOrderField());
        }

        List<BidVendorAttachment> bidVendorAttachmentList = queryBidVendorAttachmentList(bidVendorAttachmentFilterDTO);
        PageInfo<BidVendorAttachment> pageInfo = new PageInfo<>(bidVendorAttachmentList);

        List<BidVendorAttachmentVO> bidVendorAttachmentVOS = this.objectConversion(bidVendorAttachmentList);
        if(CollectionUtil.isEmpty(bidVendorAttachmentList)){
            return null;
        }
        bidVendorAttachmentVOS.forEach(bidVendorAttachmentVO -> {
            bidVendorAttachmentVO.setBidVendorVO(bidVendorService.queryBidVendorById(bidVendorAttachmentVO.getBidVendorId()));
            bidVendorAttachmentVO.setSysUserInfoVO(permService.queryByUserName(bidVendorAttachmentVO.getCreatedUser()));
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(bidVendorAttachmentList));

        return pageVO;
    }
}
