package com.xdcplus.vendor.generator.impl;

import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidMaterialMandateMapper;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.vendor.common.pojo.query.PaidMaterialMandateQuery;
import com.xdcplus.vendor.generator.PaidMaterialMandateBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.ztb.common.tool.utils.BeanUtils;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务基础实现类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:29
 */
public class PaidMaterialMandateBaseServiceImpl<S, T, E, M extends BaseMapper<E>> extends BaseServiceImpl<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate, PaidMaterialMandateMapper> implements PaidMaterialMandateBaseService<S, T, E> {

    @Autowired
    protected PaidMaterialMandateMapper paidMaterialMandateMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean savePaidMaterialMandate(PaidMaterialMandateDTO paidMaterialMandateDTO) {

        PaidMaterialMandate paidMaterialMandate = paidMaterialMandateMapper.selectById(paidMaterialMandateDTO.getId());
        if (ObjectUtil.isNotNull(paidMaterialMandate)) {
            log.error("savePaidMaterialMandate() The PaidMaterialMandate already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        paidMaterialMandate = new PaidMaterialMandate();
        BeanUtil.copyProperties(paidMaterialMandateDTO, paidMaterialMandate);
        paidMaterialMandate.setCreatedTime(DateUtil.current());
        paidMaterialMandate.setDeleted(0);

        return this.save(paidMaterialMandate);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean updatePaidMaterialMandate(PaidMaterialMandateDTO paidMaterialMandateDTO) {

        PaidMaterialMandate paidMaterialMandate = this.getById(paidMaterialMandateDTO.getId());
        if (ObjectUtil.isNull(paidMaterialMandate)) {
            log.error("updatePaidMaterialMandate() The PaidMaterialMandate does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        BeanUtil.copyProperties(paidMaterialMandateDTO, paidMaterialMandate);
        paidMaterialMandate.setUpdatedUser(paidMaterialMandateDTO.getUpdatedUser());
        paidMaterialMandate.setUpdatedTime(DateUtil.current());

        return this.updateById(paidMaterialMandate);
    }

    @Override
    public Boolean saveOrUpdateBatch(List<PaidMaterialMandate> paidMaterialMandateList) {

        paidMaterialMandateList.forEach(paidMaterialMandate -> {
            PaidMaterialMandate paidMaterialMandateParam = new PaidMaterialMandate();
            paidMaterialMandateParam.setId(paidMaterialMandate.getId());
            if (ObjectUtil.isNotNull(paidMaterialMandate.getId())) {
                paidMaterialMandate.setId(paidMaterialMandate.getId());
                paidMaterialMandate.setUpdatedTime(DateUtil.current());
                LambdaUpdateWrapper<PaidMaterialMandate> lambdaUpdate = Wrappers.lambdaUpdate();
                lambdaUpdate.eq(PaidMaterialMandate::getId, paidMaterialMandate.getId());
                update(paidMaterialMandate, lambdaUpdate);
            } else {
                paidMaterialMandate.setCreatedTime(DateUtil.current());
                paidMaterialMandate.setDeleted(0);
                save(paidMaterialMandate);
            }
        });
        return true;
    }

    @Override
    public Boolean saveOrUpdateBatchByDTOList(List<PaidMaterialMandateDTO> paidMaterialMandateDTOList) {

        List<PaidMaterialMandate> paidMaterialMandateList = BeanUtils.copyProperties(paidMaterialMandateDTOList, PaidMaterialMandate.class);
        return saveOrUpdateBatch(paidMaterialMandateList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deletePaidMaterialMandateLogical(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        PaidMaterialMandate paidMaterialMandate = this.getById(id);

        if (ObjectUtil.isNull(paidMaterialMandate)) {
            log.error("deletePaidMaterialMandate() The PaidMaterialMandate does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        paidMaterialMandate.setDeleted(1);

        return this.updateById(paidMaterialMandate);
    }

    private List<PaidMaterialMandate> queryPaidMaterialMandateList(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO) {
        paidMaterialMandateFilterDTO.setDeleted(0);
        PaidMaterialMandateQuery paidMaterialMandateQuery = BeanUtil.copyProperties(paidMaterialMandateFilterDTO, PaidMaterialMandateQuery.class);

        return paidMaterialMandateMapper.queryPaidMaterialMandate(paidMaterialMandateQuery);
    }

    @Override
    public List<PaidMaterialMandateVO> queryPaidMaterialMandateVOList(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO) {
        paidMaterialMandateFilterDTO.setDeleted(0);
        return this.objectConversion(queryPaidMaterialMandateList(paidMaterialMandateFilterDTO));
    }

    @Override
    public PageVO<PaidMaterialMandateVO> queryPaidMaterialMandate(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO) {
        paidMaterialMandateFilterDTO.setDeleted(0);
        PageVO<PaidMaterialMandateVO> pageVO = new PageVO<>();

        if (paidMaterialMandateFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidMaterialMandateFilterDTO.getCurrentPage(), paidMaterialMandateFilterDTO.getPageSize(),
                    paidMaterialMandateFilterDTO.getOrderType(), paidMaterialMandateFilterDTO.getOrderField());
        }

        List<PaidMaterialMandate> paidMaterialMandateList = queryPaidMaterialMandateList(paidMaterialMandateFilterDTO);

        PageInfo<PaidMaterialMandate> pageInfo = new PageInfo<>(paidMaterialMandateList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(paidMaterialMandateList));

        return pageVO;
    }

    @Override
    public PaidMaterialMandateVO queryPaidMaterialMandateById(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return this.objectConversion(this.getById(id));
    }


}
