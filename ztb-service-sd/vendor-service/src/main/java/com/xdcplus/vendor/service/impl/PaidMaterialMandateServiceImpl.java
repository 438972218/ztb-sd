package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateDTO;
import com.xdcplus.vendor.generator.impl.PaidMaterialMandateBaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidMaterialMandateMapper;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.PaidMaterialMandateService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:29
 */
@Slf4j
@Service("paidMaterialMandateService")
public class PaidMaterialMandateServiceImpl extends PaidMaterialMandateBaseServiceImpl<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate, PaidMaterialMandateMapper> implements PaidMaterialMandateService {

    @Autowired
    PermService permService;

    @Override
    public Boolean savePaidMaterialMandateWithUser(PaidMaterialMandateDTO paidMaterialMandateDTO) {
        PaidMaterialMandate paidMaterialMandate = paidMaterialMandateMapper.selectById(paidMaterialMandateDTO.getId());
        if (ObjectUtil.isNotNull(paidMaterialMandate)) {
            log.error("savePaidMaterialMandate() The PaidMaterialMandate already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        if(paidMaterialMandateDTO.getPaidVendorUserName()!=null){
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidMaterialMandateDTO.getPaidVendorUserName());
            paidMaterialMandateDTO.setPaidVendorUserId(sysUserInfoVO.getId());
        }

        paidMaterialMandate = new PaidMaterialMandate();
        BeanUtil.copyProperties(paidMaterialMandateDTO, paidMaterialMandate);
        paidMaterialMandate.setCreatedTime(DateUtil.current());
        paidMaterialMandate.setDeleted(0);

        return this.save(paidMaterialMandate);
    }
}
