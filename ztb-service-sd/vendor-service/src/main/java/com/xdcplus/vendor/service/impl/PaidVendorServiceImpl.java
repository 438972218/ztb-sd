package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.PaidVendor;
import com.xdcplus.vendor.common.pojo.vo.PaidSheetVO;
import com.xdcplus.vendor.common.pojo.vo.PaidVendorVO;
import com.xdcplus.vendor.generator.impl.PaidVendorBaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidVendorMapper;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.PaidSheetService;
import com.xdcplus.vendor.service.PaidVendorService;
import com.xdcplus.vendor.service.VendorUserService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 竞价供应商(PaidVendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-31 09:51:42
 */
@Slf4j
@Service("paidVendorService")
public class PaidVendorServiceImpl extends PaidVendorBaseServiceImpl<PaidVendor, PaidVendorVO, PaidVendor, PaidVendorMapper> implements PaidVendorService {

    @Autowired
    PermService permService;

    @Autowired
    VendorUserService vendorUserService;

    @Autowired
    PaidSheetService paidSheetService;

    @Override
    public PaidVendorVO findPaidVendorVOByUserNameAndSheetId(String userName, Long paidSheetId) {
        SysUserInfoVO sysUserInfoVO = permService.queryByUserName(userName);
        if (ObjectUtil.isEmpty(sysUserInfoVO)) {
            log.error("findVendorVOByUserNameAndSheetId() sysUserInfoVO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetId);
        paidVendorFilterDTO.setVendorUserId(sysUserInfoVO.getId());
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            log.error("findVendorVOByUserNameAndSheetId() paidVendorVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        return paidVendorVOS.get(NumberConstant.ZERO);

    }

    @Override
    public PaidVendorVO findPaidVendorVOByUserNameAndRequestId(String userName, Long requestId) {
        SysUserInfoVO sysUserInfoVO = permService.queryByUserName(userName);
        if (ObjectUtil.isEmpty(sysUserInfoVO)) {
            log.error("findPaidVendorVOByUserNameAndRequestId() sysUserInfoVO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(requestId);
        List<PaidSheetVO> paidSheetVOS = paidSheetService.queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            log.error("findPaidVendorVOByUserNameAndRequestId() paidSheetVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        PaidSheetVO paidSheetVO = paidSheetVOS.get(NumberConstant.ZERO);

        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVO.getId());
        paidVendorFilterDTO.setVendorUserId(sysUserInfoVO.getId());
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            log.error("findVendorVOByUserNameAndSheetId() paidVendorVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        return paidVendorVOS.get(NumberConstant.ZERO);
    }

    @Override
    public List<PaidVendorVO> findPaidVendorVOByRequestId(Long requestId) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(requestId);
        List<PaidSheetVO> paidSheetVOS = paidSheetService.queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVOS.get(NumberConstant.ZERO).getId());
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            return null;
        }
        paidVendorVOS.forEach(paidVendorVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidVendorVO.getVendorUserId());
            if (ObjectUtil.isNotEmpty(sysUserInfoVO)) {
                paidVendorVO.setVendorUserName(sysUserInfoVO.getUserName());
            }
        });

        return paidVendorVOS;
    }

    @Override
    public Map<Long, List<PaidVendorVO>> findPaidVendorVOMapByRequestId(Long requestId) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(requestId);
        List<PaidSheetVO> paidSheetVOS = paidSheetService.queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVOS.get(NumberConstant.ZERO).getId());
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            return null;
        }
//        paidVendorVOS.forEach(paidVendorVO -> {
//            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidVendorVO.getVendorUserId());
//            if (ObjectUtil.isNotEmpty(sysUserInfoVO)) {
//                paidVendorVO.setVendorUserName(sysUserInfoVO.getUserName());
//            }
//        });
        Map<Long ,List<PaidVendorVO>> map = paidVendorVOS.stream().collect(Collectors.groupingBy(PaidVendorVO::getVendorId));

        return map;
    }

    @Override
    public PaidVendorVO findPaidVendorVOByRequestIdAndUserId(Long requestId, Long userId) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(requestId);
        List<PaidSheetVO> paidSheetVOS = paidSheetService.queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVOS.get(NumberConstant.ZERO).getId());
        paidVendorFilterDTO.setVendorUserId(userId);
        List<PaidVendorVO> paidVendorVOS = queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            return null;
        }
        PaidVendorVO paidVendorVO = paidVendorVOS.get(NumberConstant.ZERO);
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(userId);
        if (ObjectUtil.isNotEmpty(sysUserInfoVO)) {
            paidVendorVO.setVendorUserName(sysUserInfoVO.getUserName());
        }

        return paidVendorVO;
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
            paidVendorVOS.forEach(paidVendorVO -> {
                SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidVendorVO.getVendorUserId());
                if (ObjectUtil.isNotEmpty(sysUserInfoVO)) {
                    paidVendorVO.setVendorUserName(sysUserInfoVO.getUserName());
                }
            });
        }
        PropertyUtils.copyProperties(pageInfo, pageVO, paidVendorVOS);

        return pageVO;
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

    private String getMark(List<String> marks) {
        while (true) {
            String mark = String.valueOf((char) ('A' + Math.random() * ('Z' - 'A' + 1)));
            if (!marks.contains(mark)) {
                return mark;
            }
        }
    }
}
