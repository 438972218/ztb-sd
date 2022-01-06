package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.biz.generator.impl.PaidVendorBaseServiceImpl;
import com.xdcplus.biz.mapper.PaidVendorMapper;
import com.xdcplus.biz.common.pojo.entity.PaidVendor;
import com.xdcplus.biz.common.pojo.vo.PaidVendorVO;
import com.xdcplus.biz.remote.service.VendorPermService;
import com.xdcplus.biz.service.PaidMaterialService;
import com.xdcplus.biz.service.PaidSheetService;
import com.xdcplus.biz.service.PaidVendorService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.CacheConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.docx4j.wml.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 竞价供应商(PaidVendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:06
 */
@Slf4j
@Service("paidVendorService")
public class PaidVendorServiceImpl extends PaidVendorBaseServiceImpl<PaidVendor, PaidVendorVO, PaidVendor, PaidVendorMapper> implements PaidVendorService {

    @Autowired
    PaidSheetService paidSheetService;

    @Autowired
    PaidMaterialService paidMaterialService;

    @Autowired
    VendorPermService vendorPermService;

    @Override
    public Boolean updatePaidVendorStatus(PaidVendorDTO paidVendorDTO) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("paid_sheet_id", paidVendorDTO.getPaidSheetId());
        PaidVendor paidVendor = new PaidVendor();
        paidVendor.setVendorStatus(paidVendorDTO.getVendorStatus());
        int result = paidVendorMapper.update(paidVendor, updateWrapper);
        if (result != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean savePaidVendorWithMark(PaidVendorDTO paidVendorDTO) {
        PaidVendor paidVendor = paidVendorMapper.selectById(paidVendorDTO.getId());
        if (ObjectUtil.isNotNull(paidVendor)) {
            log.error("savePaidVendor() The PaidVendor already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        paidVendor = new PaidVendor();
        BeanUtil.copyProperties(paidVendorDTO, paidVendor);
        paidVendor.setCreatedTime(DateUtil.current());
        paidVendor.setDeleted(0);

        //生成mark作为竞价监控器中排名显示使用
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidVendorDTO.getPaidSheetId());
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);

        //判断供应商是否存在
        if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
            List<Long> vendorIds = paidVendorVOS.stream().map(PaidVendorVO::getVendorId).collect(Collectors.toList());
            if (vendorIds.contains(paidVendorDTO.getVendorId())) {
                log.error("savePaidVendor() paidVendorVOS already exists");
                throw new ZtbWebException(ResponseEnum.PAID_VENDOR_SELECT_EXIST);
            }
        }

        String mark = null;
        if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
            List<String> marks = paidVendorVOS.stream().map(PaidVendorVO::getVendorMark).collect(Collectors.toList());
            mark = getMark(marks);
        } else {
            mark = String.valueOf((char) ('A' + Math.random() * ('Z' - 'A' + 1)));
        }
        paidVendor.setVendorMark(mark);

        return this.save(paidVendor);
    }

    @Override
    public void refreshPaidVendorMark(List<PaidVendorVO> paidVendorVOS) {
        List<String> vendorMarks = paidVendorVOS.stream().map(PaidVendorVO::getVendorMark).collect(Collectors.toList());
        String mark = null;
        List<PaidVendor> paidVendorDTOS =CollectionUtil.newArrayList();
        for (PaidVendorVO paidVendorVO : paidVendorVOS) {
            mark = getMark(vendorMarks);
            vendorMarks.add(mark);
            paidVendorVO.setVendorMark(mark);
            paidVendorDTOS.add(BeanUtil.copyProperties(paidVendorVO,PaidVendor.class));
        }
        saveOrUpdateBatch(paidVendorDTOS);
    }

    @Override
    public Boolean updatePaidVendorJudgeStartPrice(PaidVendorDTO paidVendorDTO) {
        PaidSheetVO paidSheetVO = paidSheetService.queryPaidSheetById(paidVendorDTO.getPaidSheetId());
        if(ZtbConstant.DIFFERENTIATE.equals(paidSheetVO.getStartPriceRule())){
            PaidMaterialFilterDTO paidMaterialFilterDTO =new PaidMaterialFilterDTO();
            paidMaterialFilterDTO.setPaidSheetId(paidVendorDTO.getPaidSheetId());
            List<PaidMaterialVO> paidMaterialVOS = paidMaterialService.queryPaidMaterialVOList(paidMaterialFilterDTO);
            if(CollectionUtil.isNotEmpty(paidMaterialVOS)){
                PaidMaterialVO paidMaterialVO =paidMaterialVOS.get(NumberConstant.ZERO);
                if(ZtbConstant.FORWARD.equals(paidSheetVO.getPaidDirection())){
                    if(paidMaterialVO.getTriggerPrice()<paidVendorDTO.getStartPrice()){
                        throw new ZtbWebException(ResponseEnum.PAID_FORWARD_PRICE_ERROR);
                    }
                }else if(ZtbConstant.INVERSION.equals(paidSheetVO.getPaidDirection())){
                    if(paidMaterialVO.getTriggerPrice()>paidVendorDTO.getStartPrice()){
                        throw new ZtbWebException(ResponseEnum.PAID_INVERSION_PRICE_ERROR);
                    }
                }
            }
        }

        return updatePaidVendor(paidVendorDTO);
    }

    @Override
    public PageVO<PaidVendorVO> waiveAuth(Long paidSheetId) {
        PaidVendorFilterDTO paidVendorFilterDTO=new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetId);
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if(CollectionUtil.isEmpty(paidVendorVOS)){
            return null;
        }
        List<PaidVendorDTO> paidVendorDTOList=CollectionUtil.newArrayList();
        paidVendorVOS.forEach(paidVendorVO -> {
            PaidVendorDTO paidVendorDTO = BeanUtil.copyProperties(paidVendorVO,PaidVendorDTO.class);
            paidVendorDTO.setVendorStatus(ZtbConstant.UNAUTHORIZED);
            paidVendorDTO.setConfirmedPrice(null);
            paidVendorDTOList.add(paidVendorDTO);
        });
        saveOrUpdateBatchByDTOList(paidVendorDTOList);

        paidVendorFilterDTO.setCurrentPage(NumberConstant.A_NEGATIVE);

        return queryPaidVendor(paidVendorFilterDTO);
    }

    @Override
    public PageVO<PaidVendorVO> queryPaidVendorWithUser(PaidVendorFilterDTO paidVendorFilterDTO) {
        paidVendorFilterDTO.setDeleted(0);
        PageVO<PaidVendorVO> pageVO = new PageVO<>();

        if (paidVendorFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidVendorFilterDTO.getCurrentPage(), paidVendorFilterDTO.getPageSize(),
                    paidVendorFilterDTO.getOrderType(), paidVendorFilterDTO.getOrderField());
        }

        List<PaidVendor> paidVendorList = queryPaidVendorList(paidVendorFilterDTO);

        PageInfo<PaidVendor> pageInfo = new PageInfo<>(paidVendorList);
        List<PaidVendorVO> paidVendorVOS = this.objectConversion(paidVendorList);
        if(CollectionUtil.isNotEmpty(paidVendorVOS)){
            for (PaidVendorVO paidVendorVO : paidVendorVOS) {
                SysUserInfoVO sysUserInfoVO = vendorPermService.sysUserQueryById(paidVendorVO.getVendorUserId());
                if(ObjectUtil.isNotEmpty(sysUserInfoVO)){
                    paidVendorVO.setVendorUserName(sysUserInfoVO.getUserName());
                }
            }
        }
        PropertyUtils.copyProperties(pageInfo, pageVO, paidVendorVOS);

        return pageVO;
    }

    private String getMark(List<String> marks) {
        while (true) {
            String mark = String.valueOf((char) ('A' + Math.random() * ('Z' - 'A' + 1)));
            if (!marks.contains(mark)) {
                return mark;
            }
        }
    }

}
