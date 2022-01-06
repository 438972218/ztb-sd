package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.common.pojo.entity.PaidSheet;
import com.xdcplus.biz.common.pojo.query.PaidSheetQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.impl.PaidSheetBaseServiceImpl;
import com.xdcplus.biz.mapper.PaidSheetMapper;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.remote.service.VendorFeignService;
import com.xdcplus.biz.service.*;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.CacheConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 竞价单(PaidSheet)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:03
 */
@Slf4j
@Service("paidSheetService")
public class PaidSheetServiceImpl extends PaidSheetBaseServiceImpl<PaidSheet, PaidSheetVO, PaidSheet, PaidSheetMapper> implements PaidSheetService {

    @Autowired
    private PaidMaterialService paidMaterialService;

    @Autowired
    private PaidVendorService paidVendorService;

    @Autowired
    private IeService ieService;

    @Autowired
    private PermService permService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ProjectSheetService projectSheetService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private VendorFeignService vendorFeignService;

    @Autowired
    private PaidAttachmentService paidAttachmentService;

    @Autowired
    private PaidMaterialMandateService paidMaterialMandateService;

    @Autowired
    WebsiteVO websiteVO;

    @Override
    public PaidSheetVO submitPaidSheet(PaidSheetDTO paidSheetDTO) {
        judgeTitle(paidSheetDTO.getTitle());

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTitle("竞价" + "-" + paidSheetDTO.getName() + "-" + DateUtil.formatDate(new Date()));
        requestDTO.setRuleId(paidSheetDTO.getRuleId());
        requestDTO.setProcessId(paidSheetDTO.getProcessId());
        requestDTO.setCreatedUser(paidSheetDTO.getCreatedUser());
        submitBeforeJudgeStatus(paidSheetDTO, requestDTO);
        RequestVO requestVO = ieService.saveRequest(requestDTO);

        paidSheetDTO.setRequestId(requestVO.getId());
        paidSheetDTO.setPaidStatus(ZtbConstant.STATUS_STAY_PAID);
        paidSheetDTO.setPaidStatusMark(NumberConstant.ZERO);
        PaidSheetVO paidSheetVO = savePaidSheetReturnVO(paidSheetDTO);
        paidSheetDTO.setId(paidSheetVO.getId());
        submitAfterJudgeStatus(paidSheetDTO);

        return showPaidSheetByRequestId(paidSheetVO.getRequestId());
    }

    @Override
    public PaidSheetVO agreePaidSheet(RequestFlowDTO requestFlowDTO) {
        ProcessTransforDTO processTransforDTO = approveBeforeJudgeStatus(requestFlowDTO);

        ResponseVO responseVO = ieService.processTransfor(processTransforDTO);

        approveAfterJudgeStatus(requestFlowDTO);

        return showPaidSheetByRequestId(requestFlowDTO.getRequestId());
    }

    @Override
    public PaidSheetVO savePaidSheetReturnVO(PaidSheetDTO paidSheetDTO) {
        PaidSheet paidSheet = paidSheetMapper.selectById(paidSheetDTO.getId());
        if (ObjectUtil.isNotNull(paidSheet)) {
            log.error("savePaidSheet() The PaidSheet already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        paidSheet = new PaidSheet();
        BeanUtil.copyProperties(paidSheetDTO, paidSheet);
        paidSheet.setCreatedTime(DateUtil.current());

        boolean result = this.save(paidSheet);
        PaidSheetVO paidSheetVO = BeanUtil.copyProperties(paidSheet, PaidSheetVO.class);
        if (result) {
            if (CollectionUtil.isNotEmpty(paidSheetDTO.getPaidMaterialDTOS())) {
                paidSheetDTO.getPaidMaterialDTOS().forEach(paidMaterialDTO -> {
                    paidMaterialDTO.setPaidSheetId(paidSheetVO.getId());
                    paidMaterialDTO.setId(null);
                        paidMaterialService.savePaidMaterialWithChild(paidMaterialDTO);
                });
            }
            if (CollectionUtil.isNotEmpty(paidSheetDTO.getPaidVendorDTOS())) {
                paidSheetDTO.getPaidVendorDTOS().forEach(paidVendorDTO -> {
                    paidVendorDTO.setPaidSheetId(paidSheetVO.getId());
                    paidVendorDTO.setId(null);
                    paidVendorDTO.setTotalPrice(null);
                    paidVendorDTO.setVendorStatus(null);
                    paidVendorDTO.setConfirmedPrice(null);
                    paidVendorService.savePaidVendorWithMark(paidVendorDTO);
                });
            }
            if (CollectionUtil.isNotEmpty(paidSheetDTO.getPaidAttachmentDTOS())) {
                paidSheetDTO.getPaidAttachmentDTOS().forEach(paidAttachmentDTO -> {
                    if(paidAttachmentDTO.getVendorVisible()!=null){
                        paidAttachmentDTO.setPaidSheetId(paidSheetVO.getId());
                        paidAttachmentDTO.setId(null);
                        paidAttachmentService.savePaidAttachment(paidAttachmentDTO);
                    }
                });
            }

            return paidSheetVO;
        } else {
            return null;
        }
    }

//    @Override
//    public PaidSheetVO showPaidSheetById(Long id) {
//        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
//        PaidSheetVO paidSheetVO = this.objectConversion(this.getById(id));
//
//        PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
//        paidMaterialFilterDTO.setPaidSheetId(id);
//        paidSheetVO.setPaidMaterialVOS(paidMaterialService.queryPaidMaterialVOList(paidMaterialFilterDTO));
//
//        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
//        paidVendorFilterDTO.setPaidSheetId(id);
//        paidSheetVO.setPaidVendorVOS(paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO));
//
//        PaidAttachmentFilterDTO paidAttachmentFilterDTO = new PaidAttachmentFilterDTO();
//        paidAttachmentFilterDTO.setPaidSheetId(id);
//        paidSheetVO.setPaidAttachmentVOS(paidAttachmentService.queryPaidAttachmentVOList(paidAttachmentFilterDTO));
//
//        return paidSheetVO;
//    }

    @Override
    public PaidSheetVO showPaidSheetByRequestId(Long id) {
        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestId(id);
        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }
        PaidSheetVO paidSheetVO = paidSheetVOS.get(NumberConstant.ZERO);

        PaidAttachmentFilterDTO paidAttachmentFilterDTO=new PaidAttachmentFilterDTO();
        paidAttachmentFilterDTO.setPaidSheetId(paidSheetVO.getId());
        List<PaidAttachmentVO> paidAttachmentVOS = paidAttachmentService.queryPaidAttachmentVOList(paidAttachmentFilterDTO);
        paidSheetVO.setPaidAttachmentVOS(paidAttachmentVOS);

        PaidVendorFilterDTO paidVendorFilterDTO=new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetVO.getId());
        List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
        paidSheetVO.setPaidVendorVOS(paidVendorVOS);

        combineMaterials(paidSheetVO);
        combineRequest(paidSheetVO);

        replaceStatus(paidSheetVO);

        return paidSheetVO;
    }

    @Override
    public PaidSheetVO showPaidSheetById(Long id) {
        PaidSheetVO paidSheetVO = queryPaidSheetById(id);

        combineMaterials(paidSheetVO);
        combineRequest(paidSheetVO);
        replaceStatus(paidSheetVO);

        return paidSheetVO;
    }

    @Override
    public PageVO<PaidSheetVO> queryPaidSheetWithRequest(PaidSheetFilterDTO paidSheetFilterDTO) {

        PageVO<PaidSheetVO> pageVO = new PageVO<>();

        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }


        //自己是成员的requestIds
        List<PaidSheet> paidSheets = paidSheetMapper.queryPaidSheetByProjectNumber(paidSheetFilterDTO.getUserId());
        if (CollectionUtil.isEmpty(paidSheets)) {
            return null;
        }
        Set<Long> requestIdsSet = paidSheets.stream().map(PaidSheet::getRequestId).collect(Collectors.toSet());

        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        if (paidSheetFilterDTO.getStatusId() != null) {
            requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());
        }
        if (paidSheetFilterDTO.getOddNumber() != null) {
            requestFilterDTO.setOddNumber(paidSheetFilterDTO.getOddNumber());
        }

        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }
        paidSheetFilterDTO.setRequestIds(requestIds);


        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);

        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }

        //替换状态
        paidSheetVOS.forEach(paidSheetVO -> {
            log.info("--------------------combineRequest(paidSheetVO)-----------------------");
            combineRequest(paidSheetVO);
            replaceStatus(paidSheetVO);
        });

        PageInfo<PaidSheetVO> pageInfo = new PageInfo<>(paidSheetVOS);
        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS);

        return pageVO;
    }

    @Override
    public PageVO<PaidSheetVO> queryPaidSheetWithRequest1(PaidSheetFilterDTO paidSheetFilterDTO) {
        if(paidSheetFilterDTO.getProjectSheetId()!=null){
            return queryPaidSheetWithRequest1_3(paidSheetFilterDTO);
        }

        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
        List<SysUserInfoVO> auditorSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        //判断是否是管理员或审计员
        if ( (CollectionUtil.isNotEmpty(adminSysUserInfoVOS) &&
                adminSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(paidSheetFilterDTO.getUserId())) ||
                (CollectionUtil.isNotEmpty(auditorSysUserInfoVOS) &&
                        auditorSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(paidSheetFilterDTO.getUserId()))) {
            if(StrUtil.isEmpty(String.valueOf(paidSheetFilterDTO.getStatusId())) && StrUtil.isEmpty(paidSheetFilterDTO.getOddNumber())){
                return queryPaidSheetWithRequest1_3(paidSheetFilterDTO);
            }else{
                return queryPaidSheetWithRequest1_4(paidSheetFilterDTO);
            }
        }

        if (StrUtil.isEmpty(String.valueOf(paidSheetFilterDTO.getStatusId())) && StrUtil.isEmpty(paidSheetFilterDTO.getOddNumber())) {
            return queryPaidSheetWithRequest1_1(paidSheetFilterDTO);
        } else {
            return queryPaidSheetWithRequest1_2(paidSheetFilterDTO);
        }
    }

    private PageVO<PaidSheetVO> queryPaidSheetWithRequest1_1(PaidSheetFilterDTO paidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(paidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            paidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("sysUserInfoVO is not exists");
            return null;
        }
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        paidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        PageVO<PaidSheetVO> pageVO = new PageVO<>();
        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }
        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }
        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheets);
        List<PaidSheetVO> paidSheetVOS = this.objectConversion(paidSheets);
        paidSheetVOS.forEach(paidSheetVO -> {
            combineRequest(paidSheetVO);
            replaceStatus(paidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS);

        return pageVO;
    }

    private PageVO<PaidSheetVO> queryPaidSheetWithRequest1_2(PaidSheetFilterDTO paidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(paidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            paidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("sysUserInfoVO cannot found");
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EXISTS);
        }
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        paidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = paidSheetVOS.stream().map(PaidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());
        requestFilterDTO.setOddNumber(paidSheetFilterDTO.getOddNumber());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        PaidSheetFilterDTO paidSheetFilterDTO1 = new PaidSheetFilterDTO();
        paidSheetFilterDTO1.setRequestIds(requestIds);
        paidSheetFilterDTO1.setKeyword(paidSheetFilterDTO.getKeyword());
        PageVO<PaidSheetVO> pageVO = new PageVO<>();
        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }

        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO1);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }
        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheets);
        List<PaidSheetVO> paidSheetVOS2 = this.objectConversion(paidSheets);
        paidSheetVOS2.forEach(paidSheetVO2 -> {
            combineRequest(paidSheetVO2);
            replaceStatus(paidSheetVO2);
        });
        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS2);

        return pageVO;
    }

    private PageVO<PaidSheetVO> queryPaidSheetWithRequest1_3(PaidSheetFilterDTO paidSheetFilterDTO) {
        PageVO<PaidSheetVO> pageVO = new PageVO<>();
        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }
        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }
        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheets);
        List<PaidSheetVO> paidSheetVOS = this.objectConversion(paidSheets);
        paidSheetVOS.forEach(paidSheetVO -> {
            combineRequest(paidSheetVO);
            replaceStatus(paidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS);

        return pageVO;
    }

    private PageVO<PaidSheetVO> queryPaidSheetWithRequest1_4(PaidSheetFilterDTO paidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(paidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            paidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("sysUserInfoVO cannot found");
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EXISTS);
        }
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        paidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = paidSheetVOS.stream().map(PaidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());
        requestFilterDTO.setOddNumber(paidSheetFilterDTO.getOddNumber());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        PaidSheetFilterDTO paidSheetFilterDTO1 = new PaidSheetFilterDTO();
        paidSheetFilterDTO1.setRequestIds(requestIds);
        paidSheetFilterDTO1.setKeyword(paidSheetFilterDTO.getKeyword());
        PageVO<PaidSheetVO> pageVO = new PageVO<>();
        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }

        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO1);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }
        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheets);
        List<PaidSheetVO> paidSheetVOS2 = this.objectConversion(paidSheets);
        paidSheetVOS2.forEach(paidSheetVO2 -> {
            combineRequest(paidSheetVO2);
            replaceStatus(paidSheetVO2);
        });
        PropertyUtils.copyProperties(pageInfo, pageVO, paidSheetVOS2);

        return pageVO;
    }

    @Override
    public PaidSheetVO queryPaidSheetWithProjectById(Long id) {
        PaidSheetVO paidSheetVO = queryPaidSheetById(id);
        if (ObjectUtil.isEmpty(paidSheetVO)) {
            return null;
        }
        combineRequest(paidSheetVO);
        replaceStatus(paidSheetVO);

        return paidSheetVO;
    }

    @Override
    public void queryPaidSheetByMonitor() {
//        PaidSheetQuery paidSheetQuery = new PaidSheetQuery();
////        paidSheetQuery.setPaidStatusMarks(Arrays.asList(1, 2));
//        paidSheetQuery.setPaidStatusMarks(ZtbConstant.PAID_SHEET_STATUSES);
        List<PaidSheet> paidSheets = paidSheetMapper.queryPaidSheetCache();

        if (CollectionUtil.isNotEmpty(paidSheets)) {
            List<PaidSheetVO> paidSheetVOS = CollectionUtil.newArrayList();
            for (PaidSheet paidSheet : paidSheets) {
                PaidSheetVO paidSheetVO = BeanUtil.copyProperties(paidSheet, PaidSheetVO.class);
                PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
                paidMaterialFilterDTO.setPaidSheetId(paidSheetVO.getId());
                paidSheetVO.setPaidMaterialVOS(paidMaterialService.queryPaidMaterialVOList(paidMaterialFilterDTO));

                SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidSheetVO.getCreatedUser());
                paidSheetVO.setSysUserInfoVO(sysUserInfoVO);

                paidSheetVOS.add(paidSheetVO);
            }
            redisUtils.set(CacheConstant.PAIDS, JSON.toJSONString(paidSheetVOS));
        }
    }

    @Override
    public void startPaidSheet() {
        long nowTime = System.currentTimeMillis();

        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setStatusId(ZtbConstant.RELEASED_STATUS_ID);
        requestFilterDTO.setCurrentPage(-1);
        PageVO<RequestVO> requestVOPageVO = ieService.findRequest(requestFilterDTO);
        if (ObjectUtil.isEmpty(requestVOPageVO) || CollectionUtil.isEmpty(requestVOPageVO.getRecords())) {
            return;
        }
        List<Long> requestIds = requestVOPageVO.getRecords().stream().map(RequestVO::getId).collect(Collectors.toList());

        PaidSheetFilterDTO paidSheetFilterDTO = new PaidSheetFilterDTO();
        paidSheetFilterDTO.setRequestIds(requestIds);
        paidSheetFilterDTO.setPaidStatusMark(NumberConstant.ZERO);
        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return;
        }
        for (PaidSheetVO paidSheetVO : paidSheetVOS) {
            Long startTime = paidSheetVO.getOfferStartTime();
            if (paidSheetVO.getPaidStatusMark().equals(NumberConstant.ZERO)) {
                if (nowTime >= startTime) {
                    paidSheetVO.setPaidStatusMark(NumberConstant.ONE);
                    paidSheetVO.setPaidStatus(ZtbConstant.STATUS_UNDERWAY);
                    paidSheetMapper.updateById(BeanUtil.copyProperties(paidSheetVO, PaidSheet.class));
                }
            }

        }
    }

    @Override
    public int selectCountByProjectId(Long id) {
        return paidSheetMapper.selectCount(
                new QueryWrapper<PaidSheet>().lambda().eq(PaidSheet::getProjectSheetId, id).eq(PaidSheet::getDeleted, 0));
    }

    @Override
    public Boolean pausePaidSheet(Long id) {
        PaidSheetDTO paidSheetDTO = new PaidSheetDTO();
        paidSheetDTO.setId(id);
        paidSheetDTO.setPauseTime(DateUtil.current());
        paidSheetDTO.setPaidStatusMark(NumberConstant.TWO);
        paidSheetDTO.setPaidStatus(ZtbConstant.STATUS_PAUSE);

        return updatePaidSheet(paidSheetDTO);
    }

    @Override
    public Boolean recoverPaidSheet(Long id) {
        PaidSheetVO paidSheetVO = queryPaidSheetById(id);
        Long nowTime = DateUtil.current();
        Long pauseTime = paidSheetVO.getPauseTime();
        Long offerEndTime = paidSheetVO.getOfferEndTime();
        Long newOfferEndTime = nowTime - pauseTime + offerEndTime;

        PaidSheetDTO paidSheetDTO = new PaidSheetDTO();
        paidSheetDTO.setId(id);
        paidSheetDTO.setOfferEndTime(newOfferEndTime);
        paidSheetDTO.setPauseTime(null);
        paidSheetDTO.setPaidStatusMark(NumberConstant.ONE);
        paidSheetDTO.setPaidStatus(ZtbConstant.STATUS_UNDERWAY);

        return updatePaidSheet(paidSheetDTO);
    }

    @Override
    public Boolean anewPaidSheet(PaidSheetDTO paidSheetDTO) {


        paidSheetDTO.setPauseTime(null);

        //删除出价记录
        PaidSheetVO paidSheetVO = queryPaidSheetById(paidSheetDTO.getId());
        Boolean result = vendorFeignService.deleteByRequestId(paidSheetVO.getRequestId());

        return updatePaidSheet(paidSheetDTO);

    }

    @Override
    public Boolean terminatePaidSheet(Long id) {
        PaidSheetDTO paidSheetDTO = new PaidSheetDTO();
        paidSheetDTO.setId(id);
        paidSheetDTO.setPaidStatusMark(NumberConstant.FOUR);
        paidSheetDTO.setPaidStatus(ZtbConstant.STATUS_TERMINATE);
        paidSheetDTO.setOfferEndTime(DateUtil.current());
        Boolean result = updatePaidSheet(paidSheetDTO);
        if (result) {
            sendMailTerminate(id);
        }

        return result;
    }

    @Override
    public PageVO<PaidSheetVO> queryPaidSheetTemplate(PaidSheetFilterDTO paidSheetFilterDTO) {
        PageVO<PaidSheetVO> pageVO = new PageVO<>();

        if (paidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(paidSheetFilterDTO.getCurrentPage(), paidSheetFilterDTO.getPageSize(),
                    paidSheetFilterDTO.getOrderType(), paidSheetFilterDTO.getOrderField());
        }

        paidSheetFilterDTO.setDeleted(0);
        PaidSheetQuery paidSheetQuery = BeanUtil.copyProperties(paidSheetFilterDTO, PaidSheetQuery.class);
        List<PaidSheet> paidSheetList = paidSheetMapper.queryPaidSheetTemplate(paidSheetQuery);

        PageInfo<PaidSheet> pageInfo = new PageInfo<>(paidSheetList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(paidSheetList));

        return pageVO;
    }

    @Override
    public void approveAfterJudgeStatus(RequestFlowDTO requestFlowDTO) {
        RequestVO requestVO = ieService.findRequestById(requestFlowDTO.getRequestId());
        long sheetId = requestFlowDTO.getId();
        if (requestVO.getStatus().getName().equals(ZtbConstant.YIFABU)) {
            PaidSheetVO paidSheetVO = queryPaidSheetById(sheetId);

            //发送邮件给项目成员
            sendMailProjectNumbers(paidSheetVO, requestVO);

            //发送邮件给供应商
            sendMailPaidVendors(sheetId, paidSheetVO, requestVO);

        } else if (requestVO.getStatus().getName().equals(ZtbConstant.TO_BE_AUTHORIZED)) {
            //更新供应商总出价
            PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
            paidVendorFilterDTO.setPaidSheetId(sheetId);
            List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
            List<PaidVendorDTO> paidVendorDTOS = CollectionUtil.newArrayList();
            for (PaidVendorVO paidVendorVO : paidVendorVOS) {
                //从offer表中获取最新的出价
                BidRankingMessageInfo bidRankingMessageInfo = vendorFeignService.getRankingByVendorUserIdAndRequestId(paidVendorVO.getVendorUserId(), requestFlowDTO.getRequestId());

                if (ObjectUtil.isNotEmpty(bidRankingMessageInfo) && CollectionUtil.isNotEmpty(bidRankingMessageInfo.getTotalRanking())) {
                    paidVendorVO.setTotalPrice(Double.valueOf(bidRankingMessageInfo.getTotalRanking().get(NumberConstant.ZERO).getMoney()));
                    paidVendorDTOS.add(BeanUtil.copyProperties(paidVendorVO, PaidVendorDTO.class));
                }
            }
            if (CollectionUtil.isNotEmpty(paidVendorDTOS)) {
                paidVendorService.saveOrUpdateBatchByDTOList(paidVendorDTOS);
            }
        }
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

    @Override
    public void judgeAuthorization(Long paidSheetId) {
        //查询出价结构
        PaidSheetVO paidSheetVO = queryPaidSheetById(paidSheetId);

        if(ZtbConstant.TOTAL_NUMBER.equals(paidSheetVO.getVendorMonitorRules())){
            //查询供应商信息
            PaidVendorFilterDTO paidVendorFilterDTO=new PaidVendorFilterDTO();
            paidVendorFilterDTO.setPaidSheetId(paidSheetId);
            List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
            if(CollectionUtil.isEmpty(paidVendorVOS)){
                log.error("judgeAuthorization() The paidVendorVOS find faild");
                throw new ZtbWebException(ResponseEnum.PAID_VENDOR_SELECT_FAIL);
            }
            paidVendorVOS = paidVendorVOS.stream().filter(paidVendorVO -> paidVendorVO.getVendorStatus()!=null).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(paidVendorVOS)){
                log.error("judgeAuthorization() The paidVendorVOS not authorization");
                throw new ZtbWebException(ResponseEnum.PAID_NOT_AUTHORIZATION);
            }
        }else{
            List<PaidMaterialVO> paidMaterialVOS = paidMaterialService.queryPaidMaterialVOBySheetId(paidSheetId);
            if(CollectionUtil.isEmpty(paidMaterialVOS)){
                log.error("judgeAuthorization() The paidMaterialVOS find faild");
                throw new ZtbWebException(ResponseEnum.PAID_MATERIAL_SELECT_FAIL);
            }
            for (PaidMaterialVO paidMaterialVO : paidMaterialVOS) {
                PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO=new PaidMaterialMandateFilterDTO();
                paidMaterialMandateFilterDTO.setPaidMaterialId(paidMaterialVO.getId());
                List<PaidMaterialMandateVO> paidMaterialMandateVOS = paidMaterialMandateService.queryPaidMaterialMandateVOList(paidMaterialMandateFilterDTO);
                if(CollectionUtil.isEmpty(paidMaterialMandateVOS)){
                    log.error("judgeAuthorization() The paidMaterialMandateVOS not authorization");
                    throw new ZtbWebException(ResponseEnum.PAID_NOT_AUTHORIZATION);
                }
            }
        }
    }

    /**
     * 提交之前的操作
     *
     * @param paidSheetDTO 工资表dto
     * @param requestDTO   请求dto
     */
    private void submitBeforeJudgeStatus(PaidSheetDTO paidSheetDTO, RequestDTO requestDTO) {
        Circulation circulation = new Circulation();
        circulation.setUserId(paidSheetDTO.getUserId());
        requestDTO.setCirculation(circulation);

        //新增策略条件
        FormFlowStrategyDTO formFlowStrategyDTO = new FormFlowStrategyDTO();
//        Object obj = JSONArray.toJSON(paidSheetDTO);
//        formFlowStrategyDTO.setStrategyConditions(obj);
        formFlowStrategyDTO.setTypeId(paidSheetDTO.getTypeId());
        requestDTO.setStrategy(formFlowStrategyDTO);

        String json = JSONObject.toJSONString(paidSheetDTO);
        Map map = JSONObject.parseObject(json, Map.class);
        formFlowStrategyDTO.setStrategyConditions(map);
    }

    /**
     * 提交之后的操作
     *
     * @param paidSheetDTO 工资表dto
     */
    private void submitAfterJudgeStatus(PaidSheetDTO paidSheetDTO) {

    }

    /**
     * 流转之前的操作
     *
     * @param requestFlowDTO 请求流dto
     * @return {@link ProcessTransforDTO}
     */
    private ProcessTransforDTO approveBeforeJudgeStatus(RequestFlowDTO requestFlowDTO) {
        RequestVO requestVO = ieService.findRequestById(requestFlowDTO.getRequestId());
        long sheetId = requestFlowDTO.getId();
        if (requestVO.getStatus().getName().equals(ZtbConstant.DAIFABU)) {
            PaidSheetVO paidSheetVO = queryPaidSheetById(sheetId);
            //判断vendor和material
            PaidVendorFilterDTO paidVendorFilterDTO=new PaidVendorFilterDTO();
            paidVendorFilterDTO.setPaidSheetId(paidSheetVO.getId());
            List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
            if(CollectionUtil.isEmpty(paidVendorVOS)){
                log.error("approveBeforeJudgeStatus() paidVendorVOS find faild");
                throw new ZtbWebException(ResponseEnum.PAID_VENDOR_SELECT_FAIL);
            }

            PaidMaterialFilterDTO paidMaterialFilterDTO=new PaidMaterialFilterDTO();
            paidMaterialFilterDTO.setPaidSheetId(paidSheetVO.getId());
            List<PaidMaterialVO> paidMaterialVOS = paidMaterialService.queryPaidMaterialVOList(paidMaterialFilterDTO);
            if(CollectionUtil.isEmpty(paidMaterialVOS)){
                log.error("approveBeforeJudgeStatus() paidMaterialVOS find faild");
                throw new ZtbWebException(ResponseEnum.PAID_MATERIAL_SELECT_FAIL);
            }

            //区分时，判断供应商上起始价格是否存在
            if(ZtbConstant.DIFFERENTIATE.equals(paidSheetVO.getStartPriceRule())){
                paidVendorVOS = paidVendorVOS.stream().filter(paidVendorVO -> paidVendorVO.getStartPrice()==null)
                        .collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(paidVendorVOS)){
                    log.error("approveBeforeJudgeStatus() paidVendorVOS find faild");
                    throw new ZtbWebException(ResponseEnum.PAID_VENDOR_PRICE_ERROR);
                }
            }
        }else if(ZtbConstant.TO_BE_AUTHORIZED.equals(requestVO.getStatus().getName())){
            judgeAuthorization(sheetId);
        }

        ProcessTransforDTO processTransforDTO = new ProcessTransforDTO();
        processTransforDTO.setFlowOption(1);
        processTransforDTO.setUserId(requestFlowDTO.getUserId());
        processTransforDTO.setRequestId(requestFlowDTO.getRequestId());
        processTransforDTO.setDescription(requestFlowDTO.getDescription());

        ProcessTransforDTO.Agree agree = new ProcessTransforDTO.Agree();

//        RequestVO requestVO = ieService.findRequestById(requestFlowDTO.getRequestId());
//        if (requestVO.getStatus().getName().equals(ZtbConstant.TOAUDIT)) {
//            Long requestId = requestFlowDTO.getRequestId();
//            List<RequestVO> requestVOS = ieService.findRequestByParentId(requestId);
//            if (CollectionUtil.isNotEmpty(requestVOS)) {
//                RequestVO childRequestVO = requestVOS.get(requestVOS.size() - 1);
//
//                AssemblyBuilder assemblyBuilder = AssemblyBuilder.builder();
//                assemblyBuilder.addParameter("requestRelationRequestStatusId",childRequestVO.getStatus().getId()+"L");
//                agree.setFlowConditions(assemblyBuilder.build());
//            }
//        }

        List<Long> roleIds = permService.queryByUserId(requestFlowDTO.getUserId());
        agree.setRoleIds(roleIds);
        processTransforDTO.setAgree(agree);

        return processTransforDTO;
    }

    private void combineMaterials(PaidSheetVO paidSheetVO) {
        PaidMaterialFilterDTO paidMaterialFilterDTO = new PaidMaterialFilterDTO();
        paidMaterialFilterDTO.setPaidSheetId(paidSheetVO.getId());
        paidMaterialFilterDTO.setParentId(Long.valueOf(NumberConstant.ZERO));
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
        }
        if (paidSheetVO.getCreatedUser() != null) {
            //创建人
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidSheetVO.getCreatedUser());
            paidSheetVO.setSysUserInfoVO(sysUserInfoVO);
        }
    }

    private void sendMailProjectNumbers(PaidSheetVO paidSheetVO, RequestVO requestVO) {
        //发送邮件给项目成员
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setProjectId(paidSheetVO.getProjectSheetId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);
        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {

            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.TWELVE));
            extraMailNotificationInfoDTO.setRequestId(requestVO.getId());
//                extraMailNotificationInfoDTO.setRequestId(132132132L);
//                extraMailNotificationInfoDTO.setFlowId(132132132L);

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("code", requestVO.getOddNumber());
            objectMap.put("platformName", websiteVO.getPlatFromName());

            projectMemberVOS.forEach(projectMemberVO -> {
                MailBeanInfo mailBeanInfo = new MailBeanInfo();
                mailBeanInfo.setMail(projectMemberVO.getEmail());
                mailBeanInfo.setName(projectMemberVO.getName());

                extraMailNotificationInfoDTO.setExtra(objectMap);
                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
                ieService.emailNotification(extraMailNotificationInfoDTO);
            });
        }
    }

    private void sendMailPaidVendors(Long sheetId, PaidSheetVO paidSheetVO, RequestVO requestVO) {
        //发送邮件给供应商
        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(sheetId);
        List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.THIRTEEN));
            extraMailNotificationInfoDTO.setRequestId(requestVO.getId());
//                extraMailNotificationInfoDTO.setRequestId(132132132L);
//                extraMailNotificationInfoDTO.setFlowId(132132132L);

            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(paidSheetVO.getProjectSheetId());

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("homepage", websiteVO.getVendorPlatFromName());
            objectMap.put("title", paidSheetVO.getTitle());
            objectMap.put("code", requestVO.getOddNumber());
            objectMap.put("projectCode", projectSheetVO.getCode());
            objectMap.put("offerStartTime", DateUtil.date(paidSheetVO.getOfferStartTime()));
            objectMap.put("url", websiteVO.getVendorPlatFromName());


            paidVendorVOS.forEach(paidVendorVO -> {
                MailBeanInfo mailBeanInfo = new MailBeanInfo();
                mailBeanInfo.setMail(paidVendorVO.getEmail());
                mailBeanInfo.setName(paidVendorVO.getLinkman());

                extraMailNotificationInfoDTO.setExtra(objectMap);
                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
                ieService.emailNotification(extraMailNotificationInfoDTO);
            });
        }
    }

    private void sendMailTerminate(Long sheetId) {
        //发送邮件给供应商
        PaidSheetVO paidSheetVO = queryPaidSheetById(sheetId);
        RequestVO requestVO = ieService.findRequestById(paidSheetVO.getRequestId());

        PaidVendorFilterDTO paidVendorFilterDTO = new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(sheetId);
        List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
        if (CollectionUtil.isNotEmpty(paidVendorVOS)) {
            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.FOURTEEN));
            extraMailNotificationInfoDTO.setRequestId(requestVO.getId());
//                extraMailNotificationInfoDTO.setRequestId(132132132L);
//                extraMailNotificationInfoDTO.setFlowId(132132132L);

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("code", requestVO.getOddNumber());
            objectMap.put("title", paidSheetVO.getTitle());


            paidVendorVOS.forEach(paidVendorVO -> {
                MailBeanInfo mailBeanInfo = new MailBeanInfo();
                mailBeanInfo.setMail(paidVendorVO.getEmail());
                mailBeanInfo.setName(paidVendorVO.getLinkman());

                extraMailNotificationInfoDTO.setExtra(objectMap);
                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
                ieService.emailNotification(extraMailNotificationInfoDTO);
            });
        }
    }

    //替换表单状态
    private void replaceStatus(PaidSheetVO paidSheetVO){
        if (paidSheetVO.getRequestStatusName().equals(ZtbConstant.STATUS_RELEASED)) {
            paidSheetVO.setRequestStatusName(paidSheetVO.getPaidStatus());
        }
    }

    /**
     * 判断标题是否存在
     *
     * @param title 标题
     */
    private void judgeTitle(String title){
        PaidSheetFilterDTO paidSheetFilterDTO=new PaidSheetFilterDTO();
        paidSheetFilterDTO.setTitle(title);
        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if(CollectionUtil.isNotEmpty(paidSheetVOS)){
            log.error("savePaidSheetReturnVO() The title already exists");
            throw new ZtbWebException(ResponseEnum.PAID_TITLE_IS_EXIST);
        }
    }

}
