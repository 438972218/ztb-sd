package com.xdcplus.statement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xdcplus.statement.common.constants.ZtbConstant;
import com.xdcplus.statement.common.pojo.entity.PaidSheet;
import com.xdcplus.statement.generator.impl.PaidSheetBaseServiceImpl;
import com.xdcplus.statement.mapper.PaidSheetMapper;
import com.xdcplus.statement.remote.service.IeService;
import com.xdcplus.statement.remote.service.PermService;
import com.xdcplus.statement.common.pojo.dto.*;
import com.xdcplus.statement.common.pojo.vo.*;
import com.xdcplus.statement.service.*;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.page.PageUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
    private PaidVendorService paidVendorService;

    @Autowired
    private IeService ieService;

    @Autowired
    private PermService permService;

    @Autowired
    private ProjectSheetService projectSheetService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Override
    public PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement(PaidSheetFilterDTO paidSheetFilterDTO) {
        //筛选条件：项目名称(paid)，状态(request)，创建人(paid)，关键字(paid)
        //          projectSheetId,statusId,      createdUser,  keyword
        //判断是否是管理员或审计员,是，并判断statusId,oddNumber是否都为空，都为空，则调用3接口，不为空，则调用4接口
        //不是管理员或审计员，并判断statusId,oddNumber是否都为空，都为空，则调用1接口，不为空，则调用2接口
        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
        List<SysUserInfoVO> auditorSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        //判断是否是管理员或审计员
        if ( (CollectionUtil.isNotEmpty(adminSysUserInfoVOS) &&
                adminSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(paidSheetFilterDTO.getUserId())) ||
             (CollectionUtil.isNotEmpty(auditorSysUserInfoVOS) &&
                auditorSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(paidSheetFilterDTO.getUserId()))) {
            if(StrUtil.isEmpty(String.valueOf(paidSheetFilterDTO.getStatusId()))){
                return queryPaidSheetWithStatement_3(paidSheetFilterDTO);
            }else{
                return queryPaidSheetWithStatement_4(paidSheetFilterDTO);
            }
        }

        if (StrUtil.isEmpty(String.valueOf(paidSheetFilterDTO.getStatusId()))) {
            return queryPaidSheetWithStatement_1(paidSheetFilterDTO);
        } else {
            return queryPaidSheetWithStatement_2(paidSheetFilterDTO);
        }
    }

    @Override
    public void exportExcelPaidSheetWithStatement(HttpServletResponse response, PaidSheetFilterDTO paidSheetFilterDTO) {
        PageVO<PaidSheetStatementVO> paidSheetStatementVOPageVO = queryPaidSheetWithStatement(paidSheetFilterDTO);
        if(paidSheetStatementVOPageVO==null || CollectionUtil.isEmpty(paidSheetStatementVOPageVO.getRecords())) {
            return;
        }
        List<PaidSheetStatementExcelVO> paidSheetStatementExcelVOS = paidSheetStatementVOPageVO.getRecords().stream().map(paidSheetStatementVO -> {
            PaidSheetStatementExcelVO paidSheetStatementExcelVO =new PaidSheetStatementExcelVO();
            if(ObjectUtil.isNotEmpty(paidSheetStatementVO.getSysUserInfoVO())){
                paidSheetStatementExcelVO.setCreatedUserName(paidSheetStatementVO.getSysUserInfoVO().getName());
            }
            paidSheetStatementExcelVO.setOddNumber(paidSheetStatementVO.getOddNumber());
            paidSheetStatementExcelVO.setTitle(paidSheetStatementVO.getTitle());
            if(ObjectUtil.isNotEmpty(paidSheetStatementVO.getProjectSheetVO())){
                paidSheetStatementExcelVO.setProjectName(paidSheetStatementVO.getProjectSheetVO().getTitle());
            }
            paidSheetStatementExcelVO.setBidStructure(paidSheetStatementVO.getBidStructure());
            paidSheetStatementExcelVO.setPaidDirection(paidSheetStatementVO.getPaidDirection());
            paidSheetStatementExcelVO.setOfferStartTime(DateUtil.formatDateTime(new Date(paidSheetStatementVO.getOfferStartTime())));
            paidSheetStatementExcelVO.setOfferEndTime(DateUtil.formatDateTime(new Date(paidSheetStatementVO.getOfferEndTime())));
            paidSheetStatementExcelVO.setParticipateNumber(paidSheetStatementVO.getParticipateNumber());
            paidSheetStatementExcelVO.setBestOffer(paidSheetStatementVO.getBestOffer());
            paidSheetStatementExcelVO.setRequestStatusName(paidSheetStatementVO.getRequestStatusName());
            paidSheetStatementExcelVO.setAuthorizedVendor(paidSheetStatementVO.getAuthorizedVendor());
            return paidSheetStatementExcelVO;
        }).collect(Collectors.toList());

        try {
            ExportExcelUtil.export(response, PaidSheetStatementExcelVO.class, ZtbConstant.PAID_STATEMENT, paidSheetStatementExcelVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement_1(PaidSheetFilterDTO paidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(paidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            if(paidSheetFilterDTO.getProjectSheetId()==null){
                paidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
            }
        }
        //判断是否根据createdUser查询，不是则，根据userId查询自己及下属
        if(paidSheetFilterDTO.getCreatedUser()==null){
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
            if(ObjectUtil.isEmpty(sysUserInfoVO)){
                log.error("sysUserInfoVO is not exists");
                return null;
            }
            List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
            Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
            paidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        }

        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }

        List<PaidSheetStatementVO> paidSheetStatementVOS = paidSheets.stream().map(paidSheet -> {
            PaidSheetStatementVO paidSheetStatementVO = BeanUtil.copyProperties(paidSheet, PaidSheetStatementVO.class);
            combineRequestWithStatement(paidSheetStatementVO);
            replaceStatus(paidSheetStatementVO);
            return paidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(paidSheetStatementVOS, paidSheetFilterDTO.getCurrentPage(),
                paidSheetFilterDTO.getPageSize() == null ? 0 : paidSheetFilterDTO.getPageSize());
    }

    private PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement_2(PaidSheetFilterDTO paidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(paidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            if(paidSheetFilterDTO.getProjectSheetId()==null){
                paidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
            }
        }
        //判断是否根据createdUser查询，不是则，根据userId查询自己及下属
        if(paidSheetFilterDTO.getCreatedUser()==null){
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(paidSheetFilterDTO.getUserId());
            if(ObjectUtil.isEmpty(sysUserInfoVO)){
                log.error("sysUserInfoVO cannot found");
                throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EXISTS);
            }
            List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
            Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
            paidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);
        }

        List<PaidSheetVO> paidSheetVOS = queryPaidSheetVOList(paidSheetFilterDTO);
        if (CollectionUtil.isEmpty(paidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = paidSheetVOS.stream().map(PaidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        PaidSheetFilterDTO paidSheetFilterDTO1 = new PaidSheetFilterDTO();
        paidSheetFilterDTO1.setRequestIds(requestIds);
        paidSheetFilterDTO1.setKeyword(paidSheetFilterDTO.getKeyword());

        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO1);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }

        List<PaidSheetStatementVO> paidSheetStatementVOS = paidSheets.stream().map(paidSheet -> {
            PaidSheetStatementVO paidSheetStatementVO = BeanUtil.copyProperties(paidSheet, PaidSheetStatementVO.class);
            combineRequestWithStatement(paidSheetStatementVO);
            replaceStatus(paidSheetStatementVO);
            return paidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(paidSheetStatementVOS, paidSheetFilterDTO.getCurrentPage(),
                paidSheetFilterDTO.getPageSize() == null ? 0 : paidSheetFilterDTO.getPageSize());
    }

    private PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement_3(PaidSheetFilterDTO paidSheetFilterDTO) {
        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO);
        if(CollectionUtil.isEmpty(paidSheets)){
            return null;
        }

        List<PaidSheetStatementVO> paidSheetStatementVOS = paidSheets.stream().map(paidSheet -> {
            PaidSheetStatementVO paidSheetStatementVO = BeanUtil.copyProperties(paidSheet, PaidSheetStatementVO.class);
            combineRequestWithStatement(paidSheetStatementVO);
            replaceStatus(paidSheetStatementVO);
            return paidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(paidSheetStatementVOS, paidSheetFilterDTO.getCurrentPage(),
                paidSheetFilterDTO.getPageSize() == null ? 0 : paidSheetFilterDTO.getPageSize());
    }

    private PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement_4(PaidSheetFilterDTO paidSheetFilterDTO) {
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setStatusId(paidSheetFilterDTO.getStatusId());

        //查询出request信息，得到requestIds
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        PaidSheetFilterDTO paidSheetFilterDTO1 = new PaidSheetFilterDTO();
        paidSheetFilterDTO1.setRequestIds(requestIds);
        paidSheetFilterDTO1.setKeyword(paidSheetFilterDTO.getKeyword());
        paidSheetFilterDTO1.setCreatedUser(paidSheetFilterDTO.getCreatedUser());
        paidSheetFilterDTO1.setProjectSheetId(paidSheetFilterDTO.getProjectSheetId());

        List<PaidSheet> paidSheets = queryPaidSheetList(paidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(paidSheets)) {
            return null;
        }

        List<PaidSheetStatementVO> paidSheetStatementVOS = paidSheets.stream().map(paidSheet -> {
            PaidSheetStatementVO paidSheetStatementVO = BeanUtil.copyProperties(paidSheet, PaidSheetStatementVO.class);
            combineRequestWithStatement(paidSheetStatementVO);
            replaceStatus(paidSheetStatementVO);
            return paidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(paidSheetStatementVOS, paidSheetFilterDTO.getCurrentPage(),
                paidSheetFilterDTO.getPageSize() == null ? 0 : paidSheetFilterDTO.getPageSize());
    }


    private void combineRequestWithStatement(PaidSheetStatementVO paidSheetStatementVO) {
        //项目信息
        if (paidSheetStatementVO.getProjectSheetId() != null) {
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(paidSheetStatementVO.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                paidSheetStatementVO.setProjectSheetVO(projectSheetVO);
            }
        }

        if (paidSheetStatementVO.getRequestId() != null) {
            //表单信息
            RequestVO requestVO = ieService.findRequestById(paidSheetStatementVO.getRequestId());
            paidSheetStatementVO.setRequestVO(requestVO);
            paidSheetStatementVO.setRequestTitle(requestVO.getTitle());
            paidSheetStatementVO.setOddNumber(requestVO.getOddNumber());
            paidSheetStatementVO.setRequestStatusName(requestVO.getStatus().getName());
        }
        if (paidSheetStatementVO.getCreatedUser() != null) {
            //创建人
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidSheetStatementVO.getCreatedUser());
            paidSheetStatementVO.setSysUserInfoVO(sysUserInfoVO);
        }

        //参与供应商数量，最优出价，授权供应商
        paidSheetStatementVO.setParticipateNumber(0L);
        PaidVendorFilterDTO paidVendorFilterDTO=new PaidVendorFilterDTO();
        paidVendorFilterDTO.setPaidSheetId(paidSheetStatementVO.getId());
        List<PaidVendorVO> paidVendorVOS = paidVendorService.queryPaidVendorVOList(paidVendorFilterDTO);
        if(CollectionUtil.isNotEmpty(paidVendorVOS)){
            //参与供应商数量
            long repliesNumber = paidVendorVOS.stream().count();
            paidSheetStatementVO.setParticipateNumber(repliesNumber);
            //最优出价
            List<Double> totalPrices =paidVendorVOS.stream()
                    .filter(paidVendorVO ->paidVendorVO.getTotalPrice()!=null).map(PaidVendorVO::getTotalPrice).collect(Collectors.toList());

            List<Double> confirmedPrices =paidVendorVOS.stream()
                    .filter(paidVendorVO ->paidVendorVO.getConfirmedPrice()!=null).map(PaidVendorVO::getConfirmedPrice)
                    .collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(totalPrices)){
                totalPrices.addAll(confirmedPrices);
                Double bestOffer=null;
                //判断竞价方向
                if(StrUtil.equals(paidSheetStatementVO.getPaidDirection(),ZtbConstant.FORWARD)){
                    bestOffer = totalPrices.stream().mapToDouble(Double::intValue).max().getAsDouble();
                }else{
                    bestOffer = totalPrices.stream().mapToDouble(Double::intValue).min().getAsDouble();
                }
                paidSheetStatementVO.setBestOffer(bestOffer);
            }
            //授权供应商
            List<PaidVendorVO> authorizedPaidVendorVOS = paidVendorVOS.stream()
                    .filter(paidVendorVO ->StrUtil.equals(paidVendorVO.getVendorStatus(),ZtbConstant.VENDOR_OUTBID))
                    .collect(Collectors.toList());
            if(CollUtil.isNotEmpty(authorizedPaidVendorVOS)){
                paidSheetStatementVO.setAuthorizedVendor(authorizedPaidVendorVOS.get(NumberConstant.ZERO).getVendorName());
            }
        }
    }

    //替换表单状态
    private void replaceStatus(PaidSheetStatementVO paidSheetStatementVO){
        if (paidSheetStatementVO.getRequestStatusName().equals(ZtbConstant.STATUS_RELEASED)) {
            paidSheetStatementVO.setRequestStatusName(paidSheetStatementVO.getPaidStatus());
        }
    }

}
