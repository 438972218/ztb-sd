package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.netflix.discovery.converters.Auto;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.biz.common.pojo.query.PaidMaterialMandateQuery;
import com.xdcplus.biz.common.pojo.vo.PaidVendorVO;
import com.xdcplus.biz.common.pojo.vo.VendorVO;
import com.xdcplus.biz.generator.impl.PaidMaterialMandateBaseServiceImpl;
import com.xdcplus.biz.mapper.PaidMaterialMandateMapper;
import com.xdcplus.biz.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.biz.service.PaidMaterialMandateService;
import com.xdcplus.biz.service.PaidVendorService;
import com.xdcplus.biz.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:32
 */
@Slf4j
@Service("paidMaterialMandateService")
public class PaidMaterialMandateServiceImpl extends PaidMaterialMandateBaseServiceImpl<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate, PaidMaterialMandateMapper> implements PaidMaterialMandateService {

    @Autowired
    VendorService vendorService;

    @Override
    public List<PaidMaterialMandateVO> queryPaidMaterialMandateVOListWithVendor(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO) {
        paidMaterialMandateFilterDTO.setDeleted(0);

        paidMaterialMandateFilterDTO.setDeleted(0);
        PaidMaterialMandateQuery paidMaterialMandateQuery = BeanUtil.copyProperties(paidMaterialMandateFilterDTO, PaidMaterialMandateQuery.class);
        List<PaidMaterialMandate> paidMaterialMandates = paidMaterialMandateMapper.queryPaidMaterialMandate(paidMaterialMandateQuery);

        List<PaidMaterialMandateVO> paidMaterialMandateVOS = this.objectConversion(paidMaterialMandates);
        if(CollectionUtil.isEmpty(paidMaterialMandateVOS)){
            return null;
        }
        paidMaterialMandateVOS.forEach(paidMaterialMandateVO -> {
            VendorVO vendorVO = vendorService.queryVendorById(paidMaterialMandateVO.getPaidVendorId());
            paidMaterialMandateVO.setVendorVO(vendorVO);
        });

        return paidMaterialMandateVOS;
    }
}
