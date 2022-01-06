package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailFilterDTO;
import com.xdcplus.biz.generator.impl.VendorKpiDetailBaseServiceImpl;
import com.xdcplus.biz.mapper.VendorKpiDetailMapper;
import com.xdcplus.biz.common.pojo.entity.VendorKpiDetail;
import com.xdcplus.biz.common.pojo.vo.VendorKpiDetailVO;
import com.xdcplus.biz.service.VendorKpiDetailService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:21
 */
@Slf4j
@Service("vendorKpiDetailService")
public class VendorKpiDetailServiceImpl extends VendorKpiDetailBaseServiceImpl<VendorKpiDetail, VendorKpiDetailVO, VendorKpiDetail, VendorKpiDetailMapper> implements VendorKpiDetailService {


    @Override
    public int deleteVendorKpiDetailByKpiId(Long id) {
        return vendorKpiDetailMapper.delete(new QueryWrapper<VendorKpiDetail>().lambda().eq(VendorKpiDetail::getVendorKpiId,id));
    }

    @Override
    public void insertListWithChild(List<VendorKpiDetailDTO> vendorKpiDetailDTOS,Long vendorKpiId) {
        for (VendorKpiDetailDTO vendorKpiDetailDTO : vendorKpiDetailDTOS) {
            vendorKpiDetailDTO.setVendorKpiId(vendorKpiId);
            VendorKpiDetailVO vendorKpiDetailVO = saveVendorKpiDetailReturnVO(vendorKpiDetailDTO);
            if(CollectionUtil.isNotEmpty(vendorKpiDetailDTO.getChildVendorKpiDetailDTOS())){
                List<VendorKpiDetailDTO> vendorKpiDetailDTOS1 =vendorKpiDetailDTO.getChildVendorKpiDetailDTOS();
                vendorKpiDetailDTOS1.forEach(vendorKpiDetailDTO1 -> {
                    vendorKpiDetailDTO1.setVendorKpiId(vendorKpiId);
                    vendorKpiDetailDTO1.setParentId(vendorKpiDetailVO.getId());
                });
                saveOrUpdateBatchByDTOList(vendorKpiDetailDTOS1);
            }
        }
    }

    @Override
    public VendorKpiDetailVO saveVendorKpiDetailReturnVO(VendorKpiDetailDTO vendorKpiDetailDTO) {
        VendorKpiDetail vendorKpiDetail = vendorKpiDetailMapper.selectById(vendorKpiDetailDTO.getId());
        if (ObjectUtil.isNotNull(vendorKpiDetail)) {
            log.error("saveVendorKpiDetailReturnVO() The VendorKpiDetail already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        vendorKpiDetail = new VendorKpiDetail();
        BeanUtil.copyProperties(vendorKpiDetailDTO, vendorKpiDetail);
        vendorKpiDetail.setCreatedTime(DateUtil.current());
        vendorKpiDetail.setDeleted(0);
        Boolean result = this.save(vendorKpiDetail);
        if(result){
            return BeanUtil.copyProperties(vendorKpiDetail, VendorKpiDetailVO.class);
        }else{
            log.error("saveVendorKpiDetailReturnVO() save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
    }

    @Override
    public List<VendorKpiDetailVO> listWithChildByKpiId(Long vendorKpiId) {

        VendorKpiDetailFilterDTO vendorKpiDetailFilterDTO =new VendorKpiDetailFilterDTO();
        vendorKpiDetailFilterDTO.setVendorKpiId(vendorKpiId);
        List<VendorKpiDetailVO> vendorKpiDetailVOS = queryVendorKpiDetailVOList(vendorKpiDetailFilterDTO);
        if(CollectionUtil.isEmpty(vendorKpiDetailVOS)){
            return null;
        }
        List<VendorKpiDetailVO> vendorKpiDetailVOS1 = vendorKpiDetailVOS.stream()
                .filter(vendorKpiDetailVO -> vendorKpiDetailVO.getParentId().equals(NumberConstant.ZERO.longValue())).collect(Collectors.toList());

        for (VendorKpiDetailVO vendorKpiDetailVO : vendorKpiDetailVOS1) {
            List<VendorKpiDetailVO> childVendorKpiDetailVOS = vendorKpiDetailVOS.stream().filter(vendorKpiDetailVO1 -> vendorKpiDetailVO1.getParentId().equals(vendorKpiDetailVO.getId())).collect(Collectors.toList());
            vendorKpiDetailVO.setChildVendorKpiDetailVOS(childVendorKpiDetailVOS);
        }


        return vendorKpiDetailVOS1;
    }
}
