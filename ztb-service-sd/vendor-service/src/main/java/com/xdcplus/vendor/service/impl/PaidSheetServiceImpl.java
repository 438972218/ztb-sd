package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.enums.DirectionOfEnum;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.PaidSheet;
import com.xdcplus.vendor.common.pojo.entity.PaidVendor;
import com.xdcplus.vendor.common.pojo.query.PaidSheetQuery;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.vendor.common.pojo.vo.PaidSheetVO;
import com.xdcplus.vendor.common.pojo.vo.PaidVendorVO;
import com.xdcplus.vendor.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.vendor.generator.impl.PaidSheetBaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidSheetMapper;
import com.xdcplus.vendor.remote.service.IeService;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.*;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ProcessConfigFilterDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.RequestFilterDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 竞价单(PaidSheet)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-31 09:40:16
 */
@Slf4j
@Service("paidSheetService")
public class PaidSheetServiceImpl extends PaidSheetBaseServiceImpl<PaidSheet, PaidSheetVO, PaidSheet, PaidSheetMapper> implements PaidSheetService {

    @Autowired
    private IeService ieService;

    @Autowired
    private PaidVendorService paidVendorService;

    @Autowired
    private ProjectSheetService projectSheetService;

    @Autowired
    private PaidMaterialService paidMaterialService;

    @Override
    public PageVO<PaidSheetVO> queryPaidSheetByVendor(PaidSheetFilterDTO paidSheetFilterDTO) {
        PageVO<PaidSheetVO> pageVO = new PageVO<>();

        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
//        requestFilterDTO.setKeyword(paidSheetFilterDTO.getKeyword());
        requestFilterDTO.setOddNumber(paidSheetFilterDTO.getOddNumber());
        requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());
        requestFilterDTO.setStatusIds(ZtbConstant.PAID_REQUEST_STATUS_LIST);
        requestFilterDTO.setCurrentPage(-1);

        PageVO<RequestVO> requestVOPageVO = ieService.findRequest(requestFilterDTO);
        List<RequestVO> requestVOS = requestVOPageVO.getRecords();
        if (CollectionUtil.isNotEmpty(requestVOS)) {
            List<Long> requestIds = requestVOS.stream().map(RequestVO::getId).collect(Collectors.toList());
            paidSheetFilterDTO.setRequestIds(requestIds);
        }else{
            return null;
        }

        //查询出竞价单信息
        List<PaidSheet> paidSheetList = queryPaidSheetListByVendor(paidSheetFilterDTO);

        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheetList);
        List<PaidSheetVO> paidSheetVOS = CollectionUtil.newArrayList();

        for (PaidSheet paidSheet : paidSheetList) {
            PaidSheetVO paidSheetVO = BeanUtil.copyProperties(paidSheet, PaidSheetVO.class);
            if (paidSheetVO.getRequestId() == null || paidSheetVO.getRequestId() == 0) {
                continue;
            }

            //项目信息
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(paidSheetVO.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                paidSheetVO.setProjectSheetVO(projectSheetVO);
            }

            //供应商状态
            List<PaidVendor> paidVendors = paidVendorService.list(new QueryWrapper<PaidVendor>()
                    .lambda().eq(PaidVendor::getVendorUserId, paidSheetFilterDTO.getVendorUserId())
                    .eq(PaidVendor::getPaidSheetId, paidSheet.getId()));

            if (CollUtil.isEmpty(paidVendors)) {
                log.error("queryPaidSheetByVendor() paidVendors select faild");
                throw new ZtbWebException(ResponseEnum.PAID_VENDOR_SELECT_FAIL);
            }
            PaidVendor paidVendor = paidVendors.get(NumberConstant.ZERO);
            paidSheetVO.setVendorStatus(paidVendor.getVendorStatus());

            RequestVO requestVO = ieService.findRequestById(paidSheetVO.getRequestId());
            if(ObjectUtil.isEmpty(requestVO)){
                continue;
            }
            paidSheetVO.setOddNumber(requestVO.getOddNumber());
            paidSheetVO.setRequestStatusName(requestVO.getStatus().getName());
            paidSheetVO.setRequestTitle(requestVO.getTitle());

            if (paidSheetVO.getRequestStatusName().equals(ZtbConstant.STATUS_RELEASED)) {
                paidSheetVO.setRequestStatusName(paidSheetVO.getPaidStatus());
            }
            paidSheetVOS.add(paidSheetVO);
        }
        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS);

        return pageVO;
    }

    @Override
    public PaidSheetVO showPaidSheetByRequestIdAndVendor(Long id, Long vendorUserId) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(id);
        List<PaidSheet> paidSheetList = queryPaidSheetListByVendor(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetList)) {
            return null;
        }
        PaidSheetVO paidSheetVO = this.objectConversion(paidSheetList.get(NumberConstant.ZERO));

        combineMaterials(paidSheetVO);
        combineRequest(paidSheetVO);
        combineVendors(paidSheetVO, vendorUserId);

        return paidSheetVO;
    }

    @Override
    public PaidSheetVO findPaidSheetByRequestId(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        PaidSheet paidSheet = paidSheetMapper.findPaidSheetByRequestId(requestId);
        PaidSheetVO paidSheetVO = this.objectConversion(paidSheet);
        if (ObjectUtil.isNotNull(paidSheetVO)) {
            combineMaterials(paidSheetVO);
            combineRequest(paidSheetVO);
        }

        return paidSheetVO;
    }

    @Override
    public Boolean findPaidSheetPaidDirection(Long requestId) {

        PaidSheetVO paidSheetVO = this.findPaidSheetByRequestId(requestId);
        if (ObjectUtil.isNotNull(paidSheetVO)) {
            String paidDirection = paidSheetVO.getPaidDirection();
            return DirectionOfEnum.POSITIVE.getValue().equals(paidDirection) ? Boolean.TRUE : Boolean.FALSE;
        }

        return Boolean.FALSE;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updatePaidStatusMarkByRequestId(Long requestId, Integer paidStatusMark) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        PaidSheet paidSheet = paidSheetMapper.findPaidSheetByRequestId(requestId);
        if (ObjectUtil.isNull(paidSheet)) {
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        String statusName = ZtbConstant.PAID_STATUS_MAP.get(paidStatusMark);

        paidSheetMapper.updatePaidStatusMarkByRequestId(requestId, paidStatusMark, statusName);
    }

    @Override
    public Long getCountdownByRequestId(Long requestId) {
        PaidSheetVO paidSheetVO = findPaidSheetByRequestId(requestId);
        if (ObjectUtil.isEmpty(paidSheetVO)) {
            return null;
        }
        if (paidSheetVO.getPaidStatusMark().equals(NumberConstant.ONE)) {
            return paidSheetVO.getOfferEndTime() - DateUtil.current();
        }
        return null;
    }

    @Override
    public boolean updatePaidSheetByRequestId(PaidSheetDTO paidSheetDTO) {

        PaidSheet paidSheet=new PaidSheet();
        paidSheet.setOfferEndTime(paidSheetDTO.getOfferEndTime());
        LambdaUpdateWrapper<PaidSheet> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.eq(PaidSheet::getRequestId, paidSheetDTO.getRequestId());

        return update(paidSheet, lambdaUpdate);
    }

    @Override
    public void refreshPaidVendorMark() {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setPaidStatusMark(NumberConstant.ONE);
        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return;
        }
        for (PaidSheetVO paidSheetVO : paidSheetVOS) {
            PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
            paidVendorFilterDTO.setPaidSheetId(paidSheetVO.getId());
            List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);

            if (CollectionUtil.isEmpty(paidVendorVOS)) {
                continue;
            }
            paidVendorService.refreshPaidVendorMark(paidVendorVOS);
        }
    }

    protected List<PaidSheet> queryPaidSheetListByVendor(PaidSheetFilterDTO paidSheetFilterDTO) {
        paidSheetFilterDTO.setDeleted(0);
        PaidSheetQuery paidSheetQuery = BeanUtil.copyProperties(paidSheetFilterDTO, PaidSheetQuery.class);

        return paidSheetMapper.queryPaidSheetByVendor(paidSheetQuery);
    }

    private void combineMaterials(PaidSheetVO paidSheetVO) {
        PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
        paidMaterialFilterDTO.setPaidSheetId(paidSheetVO.getId());
        List<PaidMaterialVO> paidMaterialVOS = paidMaterialService.queryPaidMaterialVOList(paidMaterialFilterDTO);
        if (CollectionUtil.isEmpty(paidMaterialVOS)) {
            return;
        }
        if (ZtbConstant.TOTAL_SUM.equals(paidSheetVO.getBidStructure())) {
            paidMaterialVOS.forEach(paidMaterialVO -> {
                PaidMaterialFilterDTO childPaidMaterialFilterDTO = new PaidMaterialFilterDTO();
                childPaidMaterialFilterDTO.setParentId(paidMaterialVO.getId());
                List<PaidMaterialVO> childPaidMaterialVOS = paidMaterialService.queryPaidMaterialVOList(childPaidMaterialFilterDTO);
                if (CollectionUtil.isNotEmpty(childPaidMaterialVOS)) {
                    paidMaterialVO.setChildPaidMaterialVOS(childPaidMaterialVOS);
                }
            });
        }
        paidSheetVO.setPaidMaterialVOS(paidMaterialVOS);
    }

    private void combineVendors(PaidSheetVO paidSheetVO, Long vendorUserId) {
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVO.getId());
        paidVendorFilterDTO.setVendorUserId(vendorUserId);
        List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isEmpty(paidVendorVOS)) {
            return;
        }
        paidSheetVO.setPaidVendorVO(paidVendorVOS.get(NumberConstant.ZERO));
    }

    private void combineRequest(PaidSheetVO paidSheetVO) {
        //项目信息
        if (paidSheetVO.getProjectSheetId() != null) {
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(paidSheetVO.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                paidSheetVO.setProjectSheetVO(projectSheetVO);
            }
        }

        if (paidSheetVO.getRequestId() != null) {
            //表单信息
            RequestVO requestVO = ieService.findRequestById(paidSheetVO.getRequestId());
            paidSheetVO.setRequestVO(requestVO);
            paidSheetVO.setRequestTitle(requestVO.getTitle());
            paidSheetVO.setOddNumber(requestVO.getOddNumber());
            paidSheetVO.setRequestStatusName(requestVO.getStatus().getName());

//            //流程信息
//            ProcessConfigFilterDTO processConfigFilterDTO = new ProcessConfigFilterDTO();
//            processConfigFilterDTO.setCurrentPage(-1);
//            processConfigFilterDTO.setProcessId(requestVO.getProcess().getId());
//            PageVO<ProcessConfigVO> processConfigVOPageVO = ieService.findProcessConfigFilter(processConfigFilterDTO);
//            List<ProcessConfigVO> processConfigVOS = processConfigVOPageVO.getRecords();
//
//            List<String> statusNames = processConfigVOS.stream().map(processConfigVO -> processConfigVO.getToStatus().getName()).distinct().collect(Collectors.toList());
//            paidSheetVO.setStatusNames(statusNames);
        }
//        if (paidSheetVO.getCreatedUser() != null) {
//            //创建人
//            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidSheetVO.getCreatedUser());
//            paidSheetVO.setSysUserInfoVO(sysUserInfoVO);
//        }
    }

}
