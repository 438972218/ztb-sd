package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialDTO;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialFilterDTO;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.biz.generator.impl.PaidMaterialBaseServiceImpl;
import com.xdcplus.biz.mapper.PaidMaterialMapper;
import com.xdcplus.biz.common.pojo.entity.PaidMaterial;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.biz.service.PaidMaterialMandateService;
import com.xdcplus.biz.service.PaidMaterialService;
import com.xdcplus.biz.service.PaidSheetService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 竞价物品(PaidMaterial)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-23 14:11:11
 */
@Slf4j
@Service("paidMaterialService")
public class PaidMaterialServiceImpl extends PaidMaterialBaseServiceImpl<PaidMaterial, PaidMaterialVO, PaidMaterial, PaidMaterialMapper> implements PaidMaterialService {

    @Autowired
    PaidSheetService paidSheetService;

    @Autowired
    PaidMaterialMandateService paidMaterialMandateService;

    @Override
    public void batchSavePaidMaterial(List<PaidMaterialDTO> paidMaterialDTOS) {
        for (PaidMaterialDTO paidMaterialDTO : paidMaterialDTOS) {
            PaidMaterial paidMaterial = BeanUtil.copyProperties(paidMaterialDTO, PaidMaterial.class);
            paidMaterial.setCreatedTime(DateUtil.current());
            paidMaterial.setDeleted(0);

            Boolean result = this.save(paidMaterial);
            if (!result) {
                log.error("batchSavePaidMaterial() paidMaterialDTO save faild");
                throw new ZtbWebException(ResponseEnum.PAID_MATERIAL_CREATE_FAIL);
            }
            List<PaidMaterialDTO> childPaidMaterialDTOS = paidMaterialDTO.getChildPaidMaterialDTOS();
            if (CollectionUtil.isNotEmpty(childPaidMaterialDTOS)) {
                childPaidMaterialDTOS.forEach(paidMaterialDTO1 -> {
                    paidMaterialDTO1.setParentId(paidMaterial.getId());
                });
                saveOrUpdateBatchByDTOList(childPaidMaterialDTOS);
            }
        }
    }

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
    public void deleteBySheetId(Long id) {
        paidMaterialMapper.delete(new QueryWrapper<PaidMaterial>().lambda().eq(PaidMaterial::getPaidSheetId,id));
    }

    @Override
    public PaidMaterialVO savePaidMaterialReturnVO(PaidMaterialDTO paidMaterialDTO) {
        PaidMaterial paidMaterial = paidMaterialMapper.selectById(paidMaterialDTO.getId());
        if (ObjectUtil.isNotNull(paidMaterial)) {
            log.error("savePaidMaterial() The PaidMaterial already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        PaidSheetVO paidSheetVO = paidSheetService.queryPaidSheetById(paidMaterialDTO.getPaidSheetId());
        if(ZtbConstant.UNIFICATION.equals(paidSheetVO.getStartPriceRule())){
            //判断起始价格和触发价格
            if(ZtbConstant.FORWARD.equals(paidSheetVO.getPaidDirection())){
                if(paidMaterialDTO.getTriggerPrice()<paidMaterialDTO.getStartPrice()){
                    throw new ZtbWebException(ResponseEnum.PAID_FORWARD_PRICE_ERROR);
                }
            }else if(ZtbConstant.INVERSION.equals(paidSheetVO.getPaidDirection())){
                if(paidMaterialDTO.getTriggerPrice()>paidMaterialDTO.getStartPrice()){
                    throw new ZtbWebException(ResponseEnum.PAID_INVERSION_PRICE_ERROR);
                }
            }
        }

        paidMaterial = new PaidMaterial();
        BeanUtil.copyProperties(paidMaterialDTO, paidMaterial);
        paidMaterial.setCreatedTime(DateUtil.current());
        paidMaterial.setDeleted(0);

        boolean result = this.save(paidMaterial);
        PaidMaterialVO paidMaterialVO = BeanUtil.copyProperties(paidMaterial, PaidMaterialVO.class);
        if (result) {
            return paidMaterialVO;
        } else {
            return null;
        }
    }

    @Override
    public void savePaidMaterialWithChild(PaidMaterialDTO paidMaterialDTO) {
        PaidMaterial paidMaterial = paidMaterialMapper.selectById(paidMaterialDTO.getId());
        if (ObjectUtil.isNotNull(paidMaterial)) {
            log.error("savePaidMaterial() The PaidMaterial already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        PaidSheetVO paidSheetVO = paidSheetService.queryPaidSheetById(paidMaterialDTO.getPaidSheetId());
        if(ZtbConstant.UNIFICATION.equals(paidSheetVO.getStartPriceRule())){
            //判断起始价格和触发价格
            if(ZtbConstant.FORWARD.equals(paidSheetVO.getPaidDirection())){
                if(paidMaterialDTO.getTriggerPrice()<paidMaterialDTO.getStartPrice()){
                    throw new ZtbWebException(ResponseEnum.PAID_FORWARD_PRICE_ERROR);
                }
            }else if(ZtbConstant.INVERSION.equals(paidSheetVO.getPaidDirection())){
                if(paidMaterialDTO.getTriggerPrice()>paidMaterialDTO.getStartPrice()){
                    throw new ZtbWebException(ResponseEnum.PAID_INVERSION_PRICE_ERROR);
                }
            }
        }

        paidMaterial = new PaidMaterial();
        BeanUtil.copyProperties(paidMaterialDTO, paidMaterial);
        paidMaterial.setCreatedTime(DateUtil.current());
        paidMaterial.setDeleted(0);

        boolean result = this.save(paidMaterial);
        PaidMaterialVO paidMaterialVO = BeanUtil.copyProperties(paidMaterial, PaidMaterialVO.class);
        if (result) {
            if(CollectionUtil.isNotEmpty(paidMaterialDTO.getChildPaidMaterialDTOS())){
                paidMaterialDTO.getChildPaidMaterialDTOS().forEach(paidMaterialDTO1 -> {
                    paidMaterialDTO1.setParentId(paidMaterialVO.getId());
                    paidMaterialDTO1.setId(null);
                    paidMaterialDTO1.setPaidSheetId(paidMaterialDTO.getPaidSheetId());
                });
                saveOrUpdateBatchByDTOList(paidMaterialDTO.getChildPaidMaterialDTOS());
            }
        }
    }

    @Override
    public List<PaidMaterialVO> getMandateByRequestId(Long requestId) {
        List<PaidMaterialVO> paidMaterialVOS = queryPaidMaterialVOByRequestId(requestId);
        if (CollectionUtil.isEmpty(paidMaterialVOS)) {
            return null;
        }
        paidMaterialVOS.forEach(paidMaterialVO -> {
            PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO = new PaidMaterialMandateFilterDTO();
            paidMaterialMandateFilterDTO.setPaidMaterialId(paidMaterialVO.getId());
            List<PaidMaterialMandateVO> paidMaterialMandateVOS = paidMaterialMandateService.queryPaidMaterialMandateVOListWithVendor(paidMaterialMandateFilterDTO);
            if (CollectionUtil.isNotEmpty(paidMaterialMandateVOS)) {
                paidMaterialVO.setPaidMaterialMandateVO(paidMaterialMandateVOS.get(NumberConstant.ZERO));
            }
        });

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


}
