package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiFilterDTO;
import com.xdcplus.biz.common.pojo.entity.VendorKpi;
import com.xdcplus.biz.common.pojo.vo.VendorKpiDetailVO;
import com.xdcplus.biz.common.pojo.vo.VendorKpiVO;
import com.xdcplus.biz.common.pojo.vo.VendorVO;
import com.xdcplus.biz.generator.impl.VendorKpiBaseServiceImpl;
import com.xdcplus.biz.mapper.VendorKpiMapper;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.VendorKpiDetailService;
import com.xdcplus.biz.service.VendorKpiService;
import com.xdcplus.biz.service.VendorService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.RequestFilterDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 供应商绩效考核表(VendorKpi)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:18
 */
@Slf4j
@Service("vendorKpiService")
public class VendorKpiServiceImpl extends VendorKpiBaseServiceImpl<VendorKpi, VendorKpiVO, VendorKpi, VendorKpiMapper> implements VendorKpiService {

    @Autowired
    VendorKpiDetailService vendorKpiDetailService;

    @Autowired
    PermService permService;

    @Autowired
    IeService ieService;

    @Autowired
    VendorService vendorService;

    @Override
    public VendorKpiVO showVendorKpiById(Long id) {
        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        VendorKpiVO vendorKpiVO = this.objectConversion(this.getById(id));

        vendorKpiVO.setVendorKpiDetailVOS(vendorKpiDetailService.listWithChildByKpiId(id));

        combineRequest(vendorKpiVO);

        return vendorKpiVO;
    }

    @Override
    public Boolean updateVendorKpiWithDetail(VendorKpiDTO vendorKpiDTO) {
        VendorKpi vendorKpi = this.getById(vendorKpiDTO.getId());
        if (ObjectUtil.isNull(vendorKpi)) {
            log.error("");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        BeanUtil.copyProperties(vendorKpiDTO, vendorKpi);

        vendorKpi.setUpdatedUser(vendorKpiDTO.getUpdatedUser());
        vendorKpi.setUpdatedTime(DateUtil.current());
        Boolean result = this.updateById(vendorKpi);
        vendorKpiDetailService.deleteVendorKpiDetailByKpiId(vendorKpiDTO.getId());

        if (CollectionUtil.isEmpty(vendorKpiDTO.getVendorKpiDetailDTOS())) {
            return result;
        }

        List<VendorKpiDetailDTO> vendorKpiDetailDTOS = vendorKpiDTO.getVendorKpiDetailDTOS();
        vendorKpiDetailService.insertListWithChild(vendorKpiDetailDTOS, vendorKpiDTO.getId());

        return result;
    }

    @Override
    public VendorKpiVO saveVendorKpiReturnVO(VendorKpiDTO vendorKpiDTO) {
        VendorKpi vendorKpi = vendorKpiMapper.selectById(vendorKpiDTO.getId());
        if (ObjectUtil.isNotNull(vendorKpi)) {
            log.error("saveVendorKpi() The VendorKpi already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        vendorKpi = new VendorKpi();
        BeanUtil.copyProperties(vendorKpiDTO, vendorKpi);
        vendorKpi.setCreatedTime(DateUtil.current());
        vendorKpi.setDeleted(0);
        boolean result = this.save(vendorKpi);
        VendorKpiVO vendorKpiVO = BeanUtil.copyProperties(vendorKpi, VendorKpiVO.class);
        if (result) {
            if (CollectionUtil.isNotEmpty(vendorKpiDTO.getVendorKpiDetailDTOS())) {
                vendorKpiDetailService.insertListWithChild(vendorKpiDTO.getVendorKpiDetailDTOS(), vendorKpiVO.getId());
            }
        }
        if (result) {
            return vendorKpiVO;
        } else {
            return null;
        }
    }

    @Override
    public void calculateScore(Long requestId) {
        VendorKpi vendorKpi = vendorKpiMapper.selectOne(new QueryWrapper<VendorKpi>().eq("request_id", requestId));
        if (ObjectUtil.isNull(vendorKpi)) {
            log.error("calculateScore() The VendorKpi does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        VendorKpiDetailFilterDTO vendorKpiDetailFilterDTO = new VendorKpiDetailFilterDTO();
        vendorKpiDetailFilterDTO.setVendorKpiId(vendorKpi.getId());
        List<VendorKpiDetailVO> vendorKpiDetailVOS = vendorKpiDetailService.queryVendorKpiDetailVOList(vendorKpiDetailFilterDTO);

        if (CollectionUtil.isEmpty(vendorKpiDetailVOS)) {
            log.error("calculateScore() The VendorKpiDetails does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
    }

    @Override
    public PageVO<VendorKpiVO> queryVendorKpiWithRequest(VendorKpiFilterDTO vendorKpiFilterDTO) {
        PageVO<VendorKpiVO> pageVO = new PageVO<>();

        if (vendorKpiFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(vendorKpiFilterDTO.getCurrentPage(), vendorKpiFilterDTO.getPageSize(),
                    vendorKpiFilterDTO.getOrderType(), vendorKpiFilterDTO.getOrderField());
        }


        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorKpiFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        if (vendorKpiFilterDTO.getStatusId() != null) {
            requestFilterDTO.setStatusId(vendorKpiFilterDTO.getStatusId());
        }
        if (vendorKpiFilterDTO.getOddNumber() != null) {
            requestFilterDTO.setOddNumber(vendorKpiFilterDTO.getOddNumber());
        }

        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }
        vendorKpiFilterDTO.setRequestIds(requestIds);


        List<VendorKpiVO> vendorKpiVOS = queryVendorKpiVOList(vendorKpiFilterDTO);

        if (CollectionUtil.isEmpty(vendorKpiVOS)) {
            return null;
        }

        vendorKpiVOS.forEach(vendorKpiVO -> combineRequest(vendorKpiVO));

        PageInfo<VendorKpiVO> pageInfo = new PageInfo<>(vendorKpiVOS);
        PropertyUtils.copyProperties(pageInfo, pageVO, vendorKpiVOS);

        return pageVO;
    }


    private void combineRequest(VendorKpiVO vendorKpiVO) {

        if (vendorKpiVO.getRequestId() != null) {
            //表单信息
            RequestVO requestVO = ieService.findRequestById(vendorKpiVO.getRequestId());
            vendorKpiVO.setRequestVO(requestVO);
            vendorKpiVO.setRequestTitle(requestVO.getTitle());
            vendorKpiVO.setOddNumber(requestVO.getOddNumber());
            vendorKpiVO.setRequestStatusName(requestVO.getStatus().getName());
        }
        if (vendorKpiVO.getCreatedUser() != null) {
            //创建人
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(vendorKpiVO.getCreatedUser());
            vendorKpiVO.setSysUserInfoVO(sysUserInfoVO);
        }
        if (vendorKpiVO.getVendorId() != null) {
            VendorVO vendorVO = vendorService.showVendorById(vendorKpiVO.getVendorId());
            vendorKpiVO.setVendorVO(vendorVO);
        }

    }

}
