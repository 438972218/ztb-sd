package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.common.pojo.entity.BidSheet;
import com.xdcplus.biz.common.pojo.entity.BidSpecialistScore;
import com.xdcplus.biz.common.pojo.entity.BidVendorDetail;
import com.xdcplus.biz.common.pojo.query.BidSheetQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.impl.BidSheetBaseServiceImpl;
import com.xdcplus.biz.mapper.BidSheetMapper;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.*;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.page.PageUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * bid招标单(BidSheet)表服务实现类
 *
 * @author makejava
 * @since 2021-08-12 14:45:42
 */
@Slf4j
@Service("bidSheetService")
public class BidSheetServiceImpl extends BidSheetBaseServiceImpl<BidSheet, BidSheetVO, BidSheet, BidSheetMapper> implements BidSheetService {

    @Autowired
    private IeService ieService;

    @Autowired
    private BidVendorService bidVendorService;

    @Autowired
    private BidVendorDetailService bidVendorDetailService;

    @Autowired
    private BidDetailService bidDetailService;

    @Autowired
    private PermService permService;

    @Autowired
    private BidSpecialistService bidSpecialistService;

    @Autowired
    private BidAttachmentService bidAttachmentService;

    @Autowired
    private BidSpecialistScoreService bidSpecialistScoreService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectSheetService projectSheetService;

    @Autowired
    WebsiteVO websiteVO;

    @Override
    public BidSheetVO submitBidSheet(BidSheetDTO bidSheetDTO) {
        judgeTitle(bidSheetDTO.getTitle());

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRuleId(bidSheetDTO.getRuleId());
        requestDTO.setTitle(ZtbConstant.BID_TITLE + "-" + bidSheetDTO.getName() + "-" + DateUtil.formatDate(new Date()));

        requestDTO.setCreatedUser(bidSheetDTO.getCreatedUser());

        submitBeforeJudgeStatus(bidSheetDTO, requestDTO);
        RequestVO requestVO = ieService.saveRequest(requestDTO);

        bidSheetDTO.setRequestId(requestVO.getId());
        BidSheetVO bidSheetVO = saveBidSheetReturnVO(bidSheetDTO);
        bidSheetDTO.setId(bidSheetVO.getId());
        submitAfterJudgeStatus(bidSheetDTO);

        return showBidSheetByRequestId(bidSheetVO.getRequestId());
    }

    @Override
    public BidSheetVO agreeBidSheet(RequestFlowDTO requestFlowDTO) {

        ProcessTransforDTO processTransforDTO = approveBeforeJudgeStatus(requestFlowDTO);
        ieService.processTransfor(processTransforDTO);
        approveAfterJudgeStatus(requestFlowDTO);

        return showBidSheetByRequestId(requestFlowDTO.getRequestId());
    }

    @Override
    public BidSheetVO backBidSheet(BidSheetDTO bidSheetDTO) {
        ProcessTransforDTO processTransforDTO = new ProcessTransforDTO();
        processTransforDTO.setFlowOption(NumberConstant.TWO);
        processTransforDTO.setRequestId(bidSheetDTO.getRequestId());
        processTransforDTO.setUserId(bidSheetDTO.getUserId());
        processTransforDTO.setDescription("退回");

        ProcessTransforDTO.SendBack sendBack = new ProcessTransforDTO.SendBack();
        sendBack.setToStatusId(bidSheetDTO.getFlowToStatusId());
        sendBack.setToUserId(bidSheetDTO.getFlowToUserId());
        processTransforDTO.setSendBack(sendBack);

        ResponseVO responseVO = ieService.processTransfor(processTransforDTO);
        //判断是否退回至正在运行
        if (ZtbConstant.RUNNING_STATUS_ID == bidSheetDTO.getFlowToStatusId()) {
            if (CollectionUtil.isEmpty(bidSheetDTO.getBidVendorDTOS())) {
                log.error("backBidSheet() The bidVendors select faild");
                throw new ZtbWebException(ResponseEnum.BID_VENDOR_DETAIL_SELECT_FAIL);
            }
            bidVendorService.returnBidVendor(bidSheetDTO);
        }

        return showBidSheetByRequestId(bidSheetDTO.getRequestId());
    }

    @Override
    public BidSheetVO saveBidSheetReturnVO(BidSheetDTO bidSheetDTO) {
        BidSheet bidSheet = bidSheetMapper.selectById(bidSheetDTO.getId());
        if (ObjectUtil.isNotNull(bidSheet)) {
            log.error("saveBidSheet() The BidSheet already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        bidSheet = new BidSheet();
        BeanUtil.copyProperties(bidSheetDTO, bidSheet);
        bidSheet.setCreatedTime(DateUtil.current());
        bidSheet.setMark(null);
        boolean result = this.save(bidSheet);
        BidSheetVO bidSheetVO = BeanUtil.copyProperties(bidSheet, BidSheetVO.class);
        if (result) {
            if (CollectionUtil.isNotEmpty(bidSheetDTO.getBidDetailDTOS())) {
                bidSheetDTO.getBidDetailDTOS().forEach(bidDetailDTO -> {
                    bidDetailDTO.setBidSheetId(bidSheetVO.getId());
                    bidDetailDTO.setId(null);
                });
                bidDetailService.saveOrUpdateBatchByDTOList(bidSheetDTO.getBidDetailDTOS());
            }
            if (CollectionUtil.isNotEmpty(bidSheetDTO.getBidVendorDTOS())) {
                bidSheetDTO.getBidVendorDTOS().forEach(bidVendorDTO -> {
                    BidVendorDTO bidVendorDTO1 =new BidVendorDTO();
                    bidVendorDTO1.setBidSheetId(bidSheetVO.getId());
                    bidVendorDTO1.setVendorId(bidVendorDTO.getVendorId());
                    bidVendorDTO1.setVendorUserId(bidVendorDTO.getVendorUserId());
                    bidVendorDTO1.setVendorName(bidVendorDTO.getVendorName());
                    bidVendorDTO1.setVendorUserName(bidVendorDTO.getVendorUserName());
                    bidVendorDTO1.setEmail(bidVendorDTO.getEmail());
                    bidVendorDTO1.setCreatedUser(bidVendorDTO.getCreatedUser());
                    bidVendorService.saveBidVendorWithDefaultReturnVO(bidVendorDTO1);
                });
            }
            if (CollectionUtil.isNotEmpty(bidSheetDTO.getBidSpecialistDTOS())) {
                bidSheetDTO.getBidSpecialistDTOS().forEach(bidSpecialistDTO -> {
                    bidSpecialistDTO.setBidSheetId(bidSheetVO.getId());
                    bidSpecialistDTO.setId(null);
                });
                bidSpecialistService.saveOrUpdateBatchByDTOList(bidSheetDTO.getBidSpecialistDTOS());
            }
            if (CollectionUtil.isNotEmpty(bidSheetDTO.getBidAttachmentDTOS())) {
                bidSheetDTO.getBidAttachmentDTOS().forEach(bidAttachmentDTO -> {
                    bidAttachmentDTO.setBidSheetId(bidSheetVO.getId());
                    bidAttachmentDTO.setId(null);
                });
                bidAttachmentService.saveOrUpdateBatchByDTOList(bidSheetDTO.getBidAttachmentDTOS());
            }

            return bidSheetVO;
        } else {
            return null;
        }
    }

    @Override
    public BidSheetVO showBidSheetByRequestId(Long id) {
        BidSheetFilterDTO bidSheetFilterDTO = new BidSheetFilterDTO();
        bidSheetFilterDTO.setRequestId(id);
        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }
        BidSheetVO bidSheetVO = bidSheetVOS.get(NumberConstant.ZERO);
        combineRequest(bidSheetVO);

        BidSpecialistFilterDTO bidSpecialistFilterDTO = new BidSpecialistFilterDTO();
        bidSpecialistFilterDTO.setBidSheetId(bidSheetVO.getId());
        bidSheetVO.setBidSpecialistVOS(bidSpecialistService.queryBidSpecialistVOList(bidSpecialistFilterDTO));

        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(bidSheetVO.getId());
        bidSheetVO.setBidDetailVOS(bidDetailService.queryBidDetailVOList(bidDetailFilterDTO));

        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(bidSheetVO.getId());
        bidSheetVO.setBidVendorVOS(bidVendorService.queryBidVendorVOList(bidVendorFilterDTO));

        return bidSheetVO;
    }

    @Override
    public PageVO<BidSheetVO> queryBidSheetWithRequest(BidSheetFilterDTO bidSheetFilterDTO) {

        //查询表单信息
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setKeyword(bidSheetFilterDTO.getKeyword());
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        requestFilterDTO.setCurrentPage(-1);

        PageVO<RequestVO> requestVOPageVO = ieService.findRequest(requestFilterDTO);
        List<RequestVO> requestVOS = requestVOPageVO.getRecords();

        if (CollectionUtil.isNotEmpty(requestVOS)) {
            List<Long> requestIds = requestVOS.stream().map(RequestVO::getId).collect(Collectors.toList());
            bidSheetFilterDTO.setRequestIds(requestIds);
        }


        List<BidSheet> bidSheetList = queryBidSheetList(bidSheetFilterDTO);

        //过滤
        List<BidSheetVO> bidSheetVOList = filterBidSheetList(bidSheetList, bidSheetFilterDTO.getUserId());
        if (CollectionUtil.isEmpty(bidSheetVOList)) {
            return null;
        }
        return PageUtils.getPageVO(bidSheetVOList, bidSheetFilterDTO.getCurrentPage(),
                bidSheetFilterDTO.getPageSize() == null ? 0 : bidSheetFilterDTO.getPageSize());

    }

    @Override
    public PageVO<BidSheetVO> queryBidSheetWithRequest2(BidSheetFilterDTO bidSheetFilterDTO) {
        PageVO<BidSheetVO> pageVO = new PageVO<>();

        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO);
        }


        //自己是成员的requestIds
        List<BidSheet> bidSheets = bidSheetMapper.queryBidSheetByProjectNumber(bidSheetFilterDTO.getUserId());
        Set<Long> requestIdsSet = bidSheets.stream().map(BidSheet::getRequestId).collect(Collectors.toSet());

        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        if (bidSheetFilterDTO.getStatusId() != null) {
            requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        }
        if (bidSheetFilterDTO.getOddNumber() != null) {
            requestFilterDTO.setOddNumber(bidSheetFilterDTO.getOddNumber());
        }

        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }
        bidSheetFilterDTO.setRequestIds(requestIds);

        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);

        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }
        bidSheetVOS.forEach(bidSheetVO -> {
            combineRequest(bidSheetVO);
        });

        PageInfo<BidSheetVO> pageInfo = new PageInfo<>(bidSheetVOS);
        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest3_1(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        bidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS = this.objectConversion(bidSheets);
        bidSheetVOS.forEach(bidSheetVO -> {
            combineRequest(bidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest3_2(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        bidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = bidSheetVOS.stream().map(BidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        requestFilterDTO.setOddNumber(bidSheetFilterDTO.getOddNumber());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        BidSheetFilterDTO bidSheetFilterDTO1 = new BidSheetFilterDTO();
        bidSheetFilterDTO1.setRequestIds(requestIds);
        bidSheetFilterDTO1.setKeyword(bidSheetFilterDTO.getKeyword());
        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }

        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS2 = this.objectConversion(bidSheets);
        bidSheetVOS2.forEach(bidSheetVO2 -> {
            combineRequest(bidSheetVO2);
        });
        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS2);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest3_3(BidSheetFilterDTO bidSheetFilterDTO) {
        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS = this.objectConversion(bidSheets);
        bidSheetVOS.forEach(bidSheetVO -> {
            combineRequest(bidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS);

        return pageVO;
    }


    @Override
    public PageVO<BidSheetVO> queryBidSheetWithRequest3(BidSheetFilterDTO bidSheetFilterDTO) {
        if (bidSheetFilterDTO.getProjectSheetId() != null) {
            return queryBidSheetWithRequest3_3(bidSheetFilterDTO);
        }

        if (StrUtil.isEmpty(String.valueOf(bidSheetFilterDTO.getStatusId())) && StrUtil.isEmpty(bidSheetFilterDTO.getOddNumber())) {
            return queryBidSheetWithRequest3_1(bidSheetFilterDTO);
        } else {
            return queryBidSheetWithRequest3_2(bidSheetFilterDTO);
        }
    }

    @Override
    public PageVO<BidSheetVO> queryBidSheetWithRequest4(BidSheetFilterDTO bidSheetFilterDTO) {
        //判断ProjectSheetId是否为空，不为空，数据则为项目界面所用
        //判断是否是管理员或审计员,是，并判断statusId,oddNumber是否都为空，都为空，则调用3接口，不为空，则调用4接口
        //不是管理员或审计员，并判断statusId,oddNumber是否都为空，都为空，则调用1接口，不为空，则调用2接口
        if (bidSheetFilterDTO.getProjectSheetId() != null) {
            return queryBidSheetWithRequest4_3(bidSheetFilterDTO);
        }

        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
        List<SysUserInfoVO> auditorSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        //判断是否是管理员或审计员
        if ( (CollectionUtil.isNotEmpty(adminSysUserInfoVOS) &&
                adminSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(bidSheetFilterDTO.getUserId())) ||
                (CollectionUtil.isNotEmpty(auditorSysUserInfoVOS) &&
                        auditorSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(bidSheetFilterDTO.getUserId()))) {
            if(StrUtil.isEmpty(String.valueOf(bidSheetFilterDTO.getStatusId())) && StrUtil.isEmpty(bidSheetFilterDTO.getOddNumber())){
                return queryBidSheetWithRequest4_3(bidSheetFilterDTO);
            }else{
                return queryBidSheetWithRequest4_4(bidSheetFilterDTO);
            }
        }else{
            if (StrUtil.isEmpty(String.valueOf(bidSheetFilterDTO.getStatusId())) && StrUtil.isEmpty(bidSheetFilterDTO.getOddNumber())) {
                return queryBidSheetWithRequest4_1(bidSheetFilterDTO);
            } else {
                return queryBidSheetWithRequest4_2(bidSheetFilterDTO);
            }
        }

    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest4_1(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("sysUserInfoVO is not exists");
            return null;
        }
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        bidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        //根据评委用户id查询出待评估招标单中评委包含自己的单子ids
        List<Long> ids = getSpecialistSheetIds(bidSheetFilterDTO.getUserId());

        if(CollectionUtil.isNotEmpty(ids)){
            bidSheetFilterDTO.setIds(ids);
        }

        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS = this.objectConversion(bidSheets);
        bidSheetVOS.forEach(bidSheetVO -> {
            combineRequest(bidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest4_2(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
        }
        //根据userId查询自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
        Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
        bidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

        //根据评委用户id查询出待评估招标单中评委包含自己的单子ids
        List<Long> ids = getSpecialistSheetIds(bidSheetFilterDTO.getUserId());

        if(CollectionUtil.isNotEmpty(ids)){
            bidSheetFilterDTO.setIds(ids);
        }

        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = bidSheetVOS.stream().map(BidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        requestFilterDTO.setOddNumber(bidSheetFilterDTO.getOddNumber());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        BidSheetFilterDTO bidSheetFilterDTO1 = new BidSheetFilterDTO();
        bidSheetFilterDTO1.setRequestIds(requestIds);
        bidSheetFilterDTO1.setKeyword(bidSheetFilterDTO.getKeyword());
        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }

        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS2 = this.objectConversion(bidSheets);
        bidSheetVOS2.forEach(bidSheetVO2 -> {
            combineRequest(bidSheetVO2);
        });
        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS2);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest4_3(BidSheetFilterDTO bidSheetFilterDTO) {
        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS = this.objectConversion(bidSheets);
        bidSheetVOS.forEach(bidSheetVO -> {
            combineRequest(bidSheetVO);
        });

        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS);

        return pageVO;
    }

    private PageVO<BidSheetVO> queryBidSheetWithRequest4_4(BidSheetFilterDTO bidSheetFilterDTO) {

        //查询出招标单信息
        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }

        //得到requestIds
        Set<Long> requestIdsSet = bidSheetVOS.stream().map(BidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        requestFilterDTO.setOddNumber(bidSheetFilterDTO.getOddNumber());

        //查询出request信息，得到requestIds
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        BidSheetFilterDTO bidSheetFilterDTO1 = new BidSheetFilterDTO();
        bidSheetFilterDTO1.setRequestIds(requestIds);
        bidSheetFilterDTO1.setKeyword(bidSheetFilterDTO.getKeyword());
        PageVO<BidSheetVO> pageVO = new PageVO<>();
        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }

        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheets);
        List<BidSheetVO> bidSheetVOS2 = this.objectConversion(bidSheets);
        bidSheetVOS2.forEach(bidSheetVO2 -> {
            combineRequest(bidSheetVO2);
        });
        PropertyUtils.copyProperties(pageInfo, pageVO, bidSheetVOS2);

        return pageVO;
    }

    /**
     * 根据评委用户id查询出待评估招标单中评委包含自己的单子ids
     *
     * @param userId 用户id
     * @return {@link List<Long>}
     */
    private List<Long> getSpecialistSheetIds(Long userId){
        //查询出所有的待评估招标单
        RequestFilterDTO requestFilterDTO =new RequestFilterDTO();
        requestFilterDTO.setStatusIds(ZtbConstant.BID_STATUS_PINGGU_IDS);
        requestFilterDTO.setCurrentPage(NumberConstant.A_NEGATIVE);
        PageVO<RequestVO> requestVOPageVO = ieService.findRequest(requestFilterDTO);
        List<RequestVO> requestVOS = requestVOPageVO.getRecords();
        if(CollectionUtil.isEmpty(requestVOS)){
            return null;
        }
        List<Long> requestIds = requestVOS.stream().map(RequestVO::getId).collect(Collectors.toList());

        //查询出招标单ids
        List<BidSheet> bidSheets = bidSheetMapper.queryBidSheetBySpecialistUserIdAndRequestIds(userId,requestIds);
        if(CollectionUtil.isEmpty(bidSheets)){
            return null;
        }
        return bidSheets.stream().map(BidSheet::getId).collect(Collectors.toList());
    }

    @Override
    public PageVO<BidSheetVO> queryBidSheetTemplate(BidSheetFilterDTO bidSheetFilterDTO) {
        PageVO<BidSheetVO> pageVO = new PageVO<>();

        if (bidSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(bidSheetFilterDTO.getCurrentPage(), bidSheetFilterDTO.getPageSize(),
                    bidSheetFilterDTO.getOrderType(), bidSheetFilterDTO.getOrderField());
        }

        bidSheetFilterDTO.setDeleted(0);
        BidSheetQuery bidSheetQuery = BeanUtil.copyProperties(bidSheetFilterDTO, BidSheetQuery.class);
        List<BidSheet> bidSheetList = bidSheetMapper.queryBidSheetTemplate(bidSheetQuery);

        PageInfo<BidSheet> pageInfo = new PageInfo<>(bidSheetList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(bidSheetList));

        return pageVO;
    }

    @Override
    public int selectCountByProjectId(Long id) {
        return bidSheetMapper.selectCount(
                new QueryWrapper<BidSheet>().lambda().eq(BidSheet::getProjectSheetId, id).eq(BidSheet::getDeleted, 0));
    }

    @Override
    public void submitBeforeJudgeStatus(BidSheetDTO bidSheetDTO, RequestDTO requestDTO) {
        Circulation circulation = new Circulation();
        circulation.setUserId(bidSheetDTO.getUserId());
        requestDTO.setCirculation(circulation);

        //新增策略条件
        FormFlowStrategyDTO formFlowStrategyDTO = new FormFlowStrategyDTO();

        String json = JSONObject.toJSONString(bidSheetDTO);
        Map map = JSONObject.parseObject(json, Map.class);
        formFlowStrategyDTO.setStrategyConditions(map);

        formFlowStrategyDTO.setTypeId(bidSheetDTO.getTypeId());

        requestDTO.setStrategy(formFlowStrategyDTO);
    }

    @Override
    public void submitAfterJudgeStatus(BidSheetDTO bidSheetDTO) {
        //复制时，不自动添加专家
        if(CollectionUtil.isEmpty(bidSheetDTO.getBidSpecialistDTOS())){
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(bidSheetDTO.getCreatedUser());
            BidSpecialistDTO bidSpecialistDTO = new BidSpecialistDTO();
            bidSpecialistDTO.setUserId(sysUserInfoVO.getId());
            bidSpecialistDTO.setCreatedUser(bidSheetDTO.getCreatedUser());
            bidSpecialistDTO.setSpecialistName(sysUserInfoVO.getName());
            bidSpecialistDTO.setBidSheetId(bidSheetDTO.getId());
            bidSpecialistService.saveBidSpecialist(bidSpecialistDTO);
        }

        //        BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetDTO.getId());
//
//        RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
//
//        if (bidSheetVO.getApprovalProcess() != null && bidSheetVO.getApprovalProcess() != 0) {
//            RequestDTO requestDTO = new RequestDTO();
//            requestDTO.setRuleId(ZtbConstant.SHENGHE_RULE_ID);
//
//            StringBuffer sb = new StringBuffer(requestVO.getTitle());
//            sb.insert(requestVO.getTitle().indexOf("-"), "审批单");
//            requestDTO.setTitle(sb.toString());
//            requestDTO.setCreatedUser(bidSheetVO.getCreatedUser());
//            HashSet<Long> parentIds = new HashSet<>();
//            parentIds.add(bidSheetVO.getRequestId());
//            requestDTO.setParentIds(parentIds);
//
//            //新增策略条件
//            FormFlowStrategyDTO formFlowStrategyDTO = new FormFlowStrategyDTO();
//            formFlowStrategyDTO.setTypeId(ZtbConstant.SHENGHE_TYPE_ID);
//            requestDTO.setStrategy(formFlowStrategyDTO);
//
//            Circulation circulation = new Circulation();
//            circulation.setUserId(bidSheetDTO.getUserId());
//            requestDTO.setCirculation(circulation);
//
//            ieService.saveRequest(requestDTO);
//        }
    }

    @Override
    public ProcessTransforDTO approveBeforeJudgeStatus(RequestFlowDTO requestFlowDTO) {
        RequestVO requestVO = ieService.findRequestById(requestFlowDTO.getRequestId());
        long sheetId = requestFlowDTO.getId();

        //新增vendorDetail和专家评分
        if (requestVO.getStatus().getName().equals(ZtbConstant.DAIFABU)) {
            saveBidVendorDetailBySheetId(sheetId);
            saveBidSpecialistScoreBySheetId(sheetId);
        }

        ProcessTransforDTO processTransforDTO = new ProcessTransforDTO();
        processTransforDTO.setFlowOption(1);
        processTransforDTO.setUserId(requestFlowDTO.getUserId());
        processTransforDTO.setRequestId(requestFlowDTO.getRequestId());
        processTransforDTO.setDescription(requestFlowDTO.getDescription());
        ProcessTransforDTO.Agree agree = new ProcessTransforDTO.Agree();

//        if (requestVO.getStatus().getName().equals(ZtbConstant.TOAUDIT)) {
//            RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
//            requestFilterDTO.setParentId(requestFlowDTO.getRequestId());
//            requestFilterDTO.setCurrentPage(-1);
//            List<RequestVO> requestVOS = ieService.findRequest(requestFilterDTO).getRecords();
//            if (CollectionUtil.isNotEmpty(requestVOS)) {
//                RequestVO childRequestVO = requestVOS.get(requestVOS.size() - 1);
//                AssemblyBuilder assemblyBuilder = AssemblyBuilder.builder();
//                assemblyBuilder.addParameter("requestRelationRequestStatusId", childRequestVO.getStatus().getId() + "L");
//                agree.setFlowConditions(assemblyBuilder.build());
//            }
//        }
        List<Long> roleIds = permService.queryByUserId(requestFlowDTO.getUserId());
        agree.setRoleIds(roleIds);
        processTransforDTO.setAgree(agree);
        return processTransforDTO;
    }

    @Override
    public void approveAfterJudgeStatus(RequestFlowDTO requestFlowDTO) {
        RequestVO requestVO = ieService.findRequestById(requestFlowDTO.getRequestId());
        long sheetId = requestFlowDTO.getId();
        if (requestVO.getStatus().getName().equals(ZtbConstant.STATUS_RUNNING)) {
            sendMailBidVendors(sheetId, requestVO);
        }
        if (requestVO.getStatus().getName().equals(ZtbConstant.TO_EVALUATE)) {
            //更新供应商状态
            BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
            bidVendorFilterDTO.setBidSheetId(requestFlowDTO.getId());
            bidVendorFilterDTO.setVendorStatus(ZtbConstant.VENDOR_NO_REPLIED);
            List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
            if (CollectionUtil.isNotEmpty(bidVendorVOS)) {
                bidVendorVOS.forEach(bidVendorVO -> {
                    bidVendorVO.setVendorStatus(ZtbConstant.HAS_REFUSED);
                    BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
                    bidVendorDetailFilterDTO.setBidSheetId(requestFlowDTO.getId());
                    bidVendorDetailFilterDTO.setBidVendorId(bidVendorVO.getId());
                    bidVendorDetailFilterDTO.setStatus(ZtbConstant.SAVE);
                    List<BidVendorDetailVO> bidVendorDetailVOS = bidVendorDetailService.queryBidVendorDetailVOList(bidVendorDetailFilterDTO);
                    if (CollectionUtil.isNotEmpty(bidVendorDetailVOS)) {
                        bidVendorDetailVOS.forEach(bidVendorDetailVO -> {
                            bidVendorDetailVO.setStatus(ZtbConstant.HISTORY);
                            bidVendorDetailService.updateBidVendorDetail(BeanUtil.copyProperties(bidVendorDetailVO, BidVendorDetailDTO.class));
                        });
                    }
                    bidVendorService.updateBidVendor(BeanUtil.copyProperties(bidVendorVO, BidVendorDTO.class));
                });
            }
        }

        if (requestVO.getStatus().getName().equals(ZtbConstant.TO_BE_AUTHORIZED)) {
            //更新供应商总评分
            BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
            bidVendorFilterDTO.setBidSheetId(sheetId);
            bidVendorFilterDTO.setVendorStatus(ZtbConstant.VENDOR_REPLIED);
            List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
            List<BidVendorDTO> bidVendorDTOS = CollectionUtil.newArrayList();
            if (CollectionUtil.isEmpty(bidVendorVOS)) {
                return;
            }
            for (BidVendorVO bidVendorVO : bidVendorVOS) {
                bidVendorVO.setTotalScore(bidVendorVO.getCommerceScore() + bidVendorVO.getQualificationScore() + bidVendorVO.getTechnologyScore());
                bidVendorDTOS.add(BeanUtil.copyProperties(bidVendorVO, BidVendorDTO.class));
            }
            bidVendorService.saveOrUpdateBatchByDTOList(bidVendorDTOS);
        }
    }

    private void saveBidVendorDetailBySheetId(long sheetId) {
        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(sheetId);
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
        if (CollectionUtil.isEmpty(bidVendorVOS)) {
            log.error("approveBeforeJudgeStatus() bidVendorVOS select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_SELECT_FAIL);
        }

        //查询出招标明细
        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(sheetId);
        List<BidDetailVO> bidDetailVOS = bidDetailService.queryBidDetailVOList(bidDetailFilterDTO);
        if (CollectionUtil.isEmpty(bidDetailVOS)) {
            log.error("approveBeforeJudgeStatus() BidDetailVO select faild");
            throw new ZtbWebException(ResponseEnum.BID_DETAIL_SELECT_ERROR);
        }

        //新增供应商明细
        List<BidVendorDetail> bidVendorDetails = CollectionUtil.newArrayList();
        for (BidVendorVO bidVendorVO : bidVendorVOS) {
            bidDetailVOS.forEach(bidDetailVO -> {
                BidVendorDetail bidVendorDetail = BeanUtil.copyProperties(bidDetailVO, BidVendorDetail.class);
                bidVendorDetail.setId(null);
                bidVendorDetail.setRound(NumberConstant.ZERO);
                bidVendorDetail.setStatus(ZtbConstant.BID_SAVE_STATUS);
                bidVendorDetail.setBidVendorId(bidVendorVO.getId());
                bidVendorDetails.add(bidVendorDetail);
            });
        }

        bidVendorDetailService.saveOrUpdateBatch(bidVendorDetails);
    }

    private void saveBidSpecialistScoreBySheetId(long sheetId) {
        BidSheetVO bidSheetVO = queryBidSheetById(sheetId);
        BidSpecialistFilterDTO bidSpecialistFilterDTO = new BidSpecialistFilterDTO();
        bidSpecialistFilterDTO.setBidSheetId(sheetId);
        List<BidSpecialistVO> bidSpecialistVOS = bidSpecialistService.queryBidSpecialistVOList(bidSpecialistFilterDTO);
        if (CollectionUtil.isEmpty(bidSpecialistVOS)) {
            log.error("approveBeforeJudgeStatus() bidSpecialistVOS select faild");
            throw new ZtbWebException(ResponseEnum.BID_SPECIALIST_SELECT_FAIL);
        }

        //查询出供应商
        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(sheetId);
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
        if (CollectionUtil.isEmpty(bidVendorVOS)) {
            log.error("approveBeforeJudgeStatus() bidVendorVOS select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_SELECT_FAIL);
        }

        //新增专家评分
        List<BidSpecialistScore> bidSpecialistScores = CollectionUtil.newArrayList();
        for (BidVendorVO bidVendorVO : bidVendorVOS) {
            insertBidSpecialistScore(bidSpecialistVOS, bidSheetVO,
                    bidVendorVO.getId(), bidVendorVO.getVendorName(), bidSpecialistScores);
        }

        bidSpecialistScoreService.saveOrUpdateBatch(bidSpecialistScores);
    }

    private void combineRequest(BidSheetVO bidSheetVO) {
        //项目信息
        if (bidSheetVO.getProjectSheetId() != null) {
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(bidSheetVO.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                bidSheetVO.setProjectSheetVO(projectSheetVO);
            }
        }

        if (bidSheetVO.getRequestId() != null) {
            RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());

            bidSheetVO.setRequestVO(requestVO);
            bidSheetVO.setRequestTitle(requestVO.getTitle());
            bidSheetVO.setOddNumber(requestVO.getOddNumber());
            bidSheetVO.setRequestStatusName(requestVO.getStatus().getName());

            if (bidSheetVO.getCreatedUser() != null) {
                //创建人
                SysUserInfoVO sysUserInfoVO = permService.queryByUserName(bidSheetVO.getCreatedUser());
                bidSheetVO.setSysUserInfoVO(sysUserInfoVO);
            }
        }
    }

    private List<BidSheetVO> filterBidSheetList(List<BidSheet> bidSheetList, Long userId) {
        List<BidSheetVO> bidSheetVOList = CollectionUtil.newArrayList();

        //自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(userId);
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());

        for (BidSheet bidSheet : bidSheetList) {
            BidSheetVO bidSheetVO = BeanUtil.copyProperties(bidSheet, BidSheetVO.class);
            //项目信息
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(bidSheet.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                bidSheetVO.setProjectSheetVO(projectSheetVO);
            }

            //表单状态
            RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
            bidSheetVO.setOddNumber(requestVO.getOddNumber());
            bidSheetVO.setRequestStatusName(requestVO.getStatus().getName());
            bidSheetVO.setRequestTitle(requestVO.getTitle());
            //待资质评估/待商业评估/待技术评估状态下专家是否包含自己
            if (ZtbConstant.BID_STATUS_PINGGU.contains(requestVO.getStatus().getName())) {
                BidSpecialistFilterDTO bidSpecialistFilterDTO = new BidSpecialistFilterDTO();
                bidSpecialistFilterDTO.setBidSheetId(bidSheetVO.getId());
                List<BidSpecialistVO> bidSpecialistVOS = bidSpecialistService.queryBidSpecialistVOList(bidSpecialistFilterDTO);
                if (CollectionUtil.isNotEmpty(bidSpecialistVOS)) {
                    List<Long> userIds = bidSpecialistVOS.stream().map(BidSpecialistVO::getUserId).collect(Collectors.toList());
                    if (userIds.contains(userId)) {
                        bidSheetVOList.add(bidSheetVO);
                    }
                }
            } else {

                //项目下成员
                ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
                projectMemberFilterDTO.setProjectId(bidSheet.getProjectSheetId());
                List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);
                if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
                    List<Long> userIds = projectMemberVOS.stream().map(ProjectMemberVO::getUserId).collect(Collectors.toList());
                    if (userIds.contains(userId)) {
                        bidSheetVOList.add(bidSheetVO);
                        continue;
                    }
                }

                //自己及下属
                if (subordinateUserNames.contains(bidSheet.getCreatedUser())) {
                    bidSheetVOList.add(bidSheetVO);
                }
            }
        }

        return bidSheetVOList;
    }

    @Override
    public List<BidSpecialistScore> insertBidSpecialistScore(List<BidSpecialistVO> bidSpecialistVOS, BidSheetVO bidSheetVO,
                                                             long bidVendorId, String bidVendorName, List<BidSpecialistScore> bidSpecialistScores) {
        //商业标
        bidSpecialistVOS.forEach(bidSpecialistVO -> {
            BidSpecialistScore bidSpecialistScore = new BidSpecialistScore();
            bidSpecialistScore.setBidVendorId(bidVendorId);
            bidSpecialistScore.setBidVendorName(bidVendorName);
            bidSpecialistScore.setUserId(bidSpecialistVO.getUserId());
            bidSpecialistScore.setBidType(ZtbConstant.COMMERCE);
            bidSpecialistScore.setWhetherView(NumberConstant.ZERO);
            bidSpecialistScore.setRound(NumberConstant.ZERO);
            bidSpecialistScore.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
            bidSpecialistScores.add(bidSpecialistScore);
        });
        //资质标
        if (NumberConstant.ONE.equals(bidSheetVO.getQualificationTender())) {
            bidSpecialistVOS.forEach(bidSpecialistVO -> {
                BidSpecialistScore bidSpecialistScore = new BidSpecialistScore();
                bidSpecialistScore.setBidVendorId(bidVendorId);
                bidSpecialistScore.setBidVendorName(bidVendorName);
                bidSpecialistScore.setUserId(bidSpecialistVO.getUserId());
                bidSpecialistScore.setBidType(ZtbConstant.QUALIFICATION);
                bidSpecialistScore.setWhetherView(NumberConstant.ZERO);
                bidSpecialistScore.setRound(NumberConstant.ZERO);
                bidSpecialistScore.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
                bidSpecialistScores.add(bidSpecialistScore);
            });
        }
        //技术标
        if (NumberConstant.ONE.equals(bidSheetVO.getTechnicalTender())) {
            bidSpecialistVOS.forEach(bidSpecialistVO -> {
                BidSpecialistScore bidSpecialistScore = new BidSpecialistScore();
                bidSpecialistScore.setBidVendorId(bidVendorId);
                bidSpecialistScore.setBidVendorName(bidVendorName);
                bidSpecialistScore.setUserId(bidSpecialistVO.getUserId());
                bidSpecialistScore.setBidType(ZtbConstant.TECHNOLOGY);
                bidSpecialistScore.setWhetherView(NumberConstant.ZERO);
                bidSpecialistScore.setRound(NumberConstant.ZERO);
                bidSpecialistScore.setStatus(ZtbConstant.SPECIALIST_SCORE_NEW);
                bidSpecialistScores.add(bidSpecialistScore);
            });
        }
        return bidSpecialistScores;
    }

    @Override
    public Boolean updateBidSheetMark(BidSheetVO bidSheetVO, String mark) {
        bidSheetVO.setMark(mark);

        return updateById(BeanUtil.copyProperties(bidSheetVO, BidSheet.class));
    }

    @Override
    public void finishBidSheet() {
        long nowTime = System.currentTimeMillis()+60000;

        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setStatusId(ZtbConstant.RUNNING_STATUS_ID);
        requestFilterDTO.setCurrentPage(-1);
        PageVO<RequestVO> requestVOPageVO = ieService.findRequest(requestFilterDTO);
        if (ObjectUtil.isEmpty(requestVOPageVO) || CollectionUtil.isEmpty(requestVOPageVO.getRecords())) {
            return;
        }
        List<Long> requestIds = requestVOPageVO.getRecords().stream().map(RequestVO::getId).collect(Collectors.toList());

        BidSheetFilterDTO bidSheetFilterDTO = new BidSheetFilterDTO();
        bidSheetFilterDTO.setRequestIds(requestIds);
        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return;
        }
        for (BidSheetVO bidSheetVO : bidSheetVOS) {
            Long bidEndTime = bidSheetVO.getBidEndTime();
            Long clarifyEndTime = bidSheetVO.getClarifyEndTime();
            if (bidEndTime == null) {
                return;
            }
            if ((clarifyEndTime != null && nowTime <= clarifyEndTime && clarifyEndTime <= nowTime+5000)
                    || (clarifyEndTime == null && nowTime <= bidEndTime && bidEndTime <= nowTime+5000 )) {
                RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
                sendMailByFinish(bidSheetVO.getId(), requestVO);
            }
//            RequestFlowDTO requestFlowDTO = new RequestFlowDTO();
//            requestFlowDTO.setUserId(ZtbConstant.ADMIN_USER_ID);
//            requestFlowDTO.setRequestId(bidSheetVO.getRequestId());
//            requestFlowDTO.setId(bidSheetVO.getId());
//            agreeBidSheet(requestFlowDTO);
        }
    }

    /**
     * 发送邮件给供应商
     *
     * @param sheetId   表id
     * @param requestVO 请求签证官
     */
    private void sendMailBidVendors(Long sheetId, RequestVO requestVO) {
        //发送邮件给供应商
        BidSheetVO bidSheetVO = queryBidSheetById(sheetId);

        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(sheetId);
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
        if (CollectionUtil.isNotEmpty(bidVendorVOS)) {
            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.EIGHT));
            extraMailNotificationInfoDTO.setRequestId(bidSheetVO.getRequestId());

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("sheetType", ZtbConstant.BID_TITLE);
            objectMap.put("code", requestVO.getOddNumber());
            objectMap.put("title", bidSheetVO.getTitle());
            objectMap.put("bidEndTime", DateUtil.date(bidSheetVO.getBidEndTime()));
            objectMap.put("platformName", websiteVO.getVendorPlatFromName());
            objectMap.put("homepage", websiteVO.getVendorPlatFromName());


            bidVendorVOS.forEach(bidVendorVO -> {
                MailBeanInfo mailBeanInfo = new MailBeanInfo();
                mailBeanInfo.setMail(bidVendorVO.getEmail());
                mailBeanInfo.setName(bidVendorVO.getVendorName());

                extraMailNotificationInfoDTO.setExtra(objectMap);
                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
                ieService.emailNotification(extraMailNotificationInfoDTO);
                try {
                    Thread.sleep( 500 );
                } catch (Exception e){
                    System.exit( 0 ); //退出程序
                }
            });

//            for (int i =1;i<bidVendorVOS.size();i++) {
//                BidVendorVO bidVendorVO = bidVendorVOS.get(i-1);
//                MailBeanInfo mailBeanInfo = new MailBeanInfo();
//                mailBeanInfo.setMail(bidVendorVO.getEmail());
//                mailBeanInfo.setName(bidVendorVO.getVendorName());
//
//                extraMailNotificationInfoDTO.setExtra(objectMap);
//                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
//                ieService.emailNotification(extraMailNotificationInfoDTO);
//                //超过3的倍数个供应商则休眠1秒
//                if(i%3==0){
//                    try {
//                        Thread.sleep( 1000 );
//                    } catch (Exception e){
//                        System.exit( 0 ); //退出程序
//                    }
//                }
//            }
        }
    }

    /**
     * 到结束时间前1分钟发送邮件
     *
     * @param sheetId   表id
     * @param requestVO 请求签证官
     */
    private void sendMailByFinish(Long sheetId, RequestVO requestVO) {
        //发送邮件给创建人
        BidSheetVO bidSheetVO = queryBidSheetById(sheetId);

        SysUserInfoVO sysUserInfoVO = permService.queryByUserName(bidSheetVO.getCreatedUser());
        ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(bidSheetVO.getProjectSheetId());

        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.TWENTY_TWO));
        extraMailNotificationInfoDTO.setRequestId(requestVO.getId());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("sheetType", ZtbConstant.BID_TITLE);
        objectMap.put("code", requestVO.getOddNumber());
        objectMap.put("title", bidSheetVO.getTitle());
        objectMap.put("projectName", projectSheetVO.getTitle());
        objectMap.put("closeTime", DateUtil.date(bidSheetVO.getBidEndTime()));
        objectMap.put("platformName", websiteVO.getPlatFromName());
        objectMap.put("url", websiteVO.getPlatFromName());

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(sysUserInfoVO.getMail());
        mailBeanInfo.setName(sysUserInfoVO.getName());

        extraMailNotificationInfoDTO.setExtra(objectMap);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
        ieService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 判断标题是否存在
     *
     * @param title 标题
     */
    private void judgeTitle(String title){
        BidSheetFilterDTO bidSheetFilterDTO=new BidSheetFilterDTO();
        bidSheetFilterDTO.setTitle(title);
        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if(CollectionUtil.isNotEmpty(bidSheetVOS)){
            log.error("saveBidSheetReturnVO() The title already exists");
            throw new ZtbWebException(ResponseEnum.BID_TITLE_IS_EXIST);
        }
    }

}
