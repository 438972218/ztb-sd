package com.xdcplus.statement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xdcplus.statement.common.constants.ZtbConstant;
import com.xdcplus.statement.common.pojo.entity.BidSheet;
import com.xdcplus.statement.generator.impl.BidSheetBaseServiceImpl;
import com.xdcplus.statement.mapper.BidSheetMapper;
import com.xdcplus.statement.remote.service.IeService;
import com.xdcplus.statement.remote.service.PermService;
import com.xdcplus.statement.common.pojo.dto.*;
import com.xdcplus.statement.common.pojo.vo.*;
import com.xdcplus.statement.service.*;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.page.PageUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
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
    private PermService permService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectSheetService projectSheetService;

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
    public PageVO<BidSheetStatementVO> queryBidSheetWithStatement(BidSheetFilterDTO bidSheetFilterDTO) {
        //筛选条件：项目名称(bid)，状态(request)，创建人(bid)，关键字(bid)
        //          projectSheetId,statusId,     createdUser, keyword
        //判断是否是管理员或审计员,是，并判断statusId是否都为空，都为空，则调用3接口，不为空，则调用4接口
        //不是管理员或审计员，并判断statusId是否都为空，都为空，则调用1接口，不为空，则调用2接口
        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
        List<SysUserInfoVO> auditorSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        //判断是否是管理员或审计员
        if ( (CollectionUtil.isNotEmpty(adminSysUserInfoVOS) &&
                adminSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(bidSheetFilterDTO.getUserId())) ||
                (CollectionUtil.isNotEmpty(auditorSysUserInfoVOS) &&
                        auditorSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(bidSheetFilterDTO.getUserId()))) {
            if(StrUtil.isEmpty(String.valueOf(bidSheetFilterDTO.getStatusId()))){
                return queryBidSheetWithStatement_3(bidSheetFilterDTO);
            }else{
                return queryBidSheetWithStatement_4(bidSheetFilterDTO);
            }
        }else{
            if (StrUtil.isEmpty(String.valueOf(bidSheetFilterDTO.getStatusId()))) {
                return queryBidSheetWithStatement_1(bidSheetFilterDTO);
            } else {
                return queryBidSheetWithStatement_2(bidSheetFilterDTO);
            }
        }
    }

    private PageVO<BidSheetStatementVO> queryBidSheetWithStatement_1(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            if(bidSheetFilterDTO.getProjectSheetId()==null){
                bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
            }
        }
        //判断是否根据createdUser查询，不是则，根据userId查询自己及下属
        if(bidSheetFilterDTO.getCreatedUser()==null){
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
        }

        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        List<BidSheetStatementVO> bidSheetStatementVOS = bidSheets.stream().map(bidSheet -> {
            BidSheetStatementVO bidSheetStatementVO = BeanUtil.copyProperties(bidSheet, BidSheetStatementVO.class);
            combineRequestWithStatement(bidSheetStatementVO);
            return bidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(bidSheetStatementVOS, bidSheetFilterDTO.getCurrentPage(),
                bidSheetFilterDTO.getPageSize() == null ? 0 : bidSheetFilterDTO.getPageSize());
    }

    private PageVO<BidSheetStatementVO> queryBidSheetWithStatement_2(BidSheetFilterDTO bidSheetFilterDTO) {
        //自己是成员的projectSheetIds
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setUserId(bidSheetFilterDTO.getUserId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);

        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
            List<Long> projectSheetIds = projectMemberVOS.stream().map(ProjectMemberVO::getProjectId).collect(Collectors.toList());
            if(bidSheetFilterDTO.getProjectSheetId()==null){
                bidSheetFilterDTO.setProjectSheetIds(projectSheetIds);
            }
        }
        //判断是否根据createdUser查询，不是则，根据userId查询自己及下属
        if(bidSheetFilterDTO.getCreatedUser()==null) {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSheetFilterDTO.getUserId());
            List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());
            Set<String> subordinateUserNamesSet = new HashSet<>(subordinateUserNames);
            bidSheetFilterDTO.setCreatedUsers(subordinateUserNamesSet);

            //根据评委用户id查询出待评估招标单中评委包含自己的单子ids
            List<Long> ids = getSpecialistSheetIds(bidSheetFilterDTO.getUserId());

            if (CollectionUtil.isNotEmpty(ids)) {
                bidSheetFilterDTO.setIds(ids);
            }
        }

        List<BidSheetVO> bidSheetVOS = queryBidSheetVOList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheetVOS)) {
            return null;
        }

        Set<Long> requestIdsSet = bidSheetVOS.stream().map(BidSheetVO::getRequestId).collect(Collectors.toSet());
        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setIds(requestIdsSet);
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        BidSheetFilterDTO bidSheetFilterDTO1 = new BidSheetFilterDTO();
        bidSheetFilterDTO1.setRequestIds(requestIds);
        bidSheetFilterDTO1.setKeyword(bidSheetFilterDTO.getKeyword());
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }

        List<BidSheetStatementVO> bidSheetStatementVOS = bidSheets.stream().map(bidSheet -> {
            BidSheetStatementVO bidSheetStatementVO = BeanUtil.copyProperties(bidSheet, BidSheetStatementVO.class);
            combineRequestWithStatement(bidSheetStatementVO);
            return bidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(bidSheetStatementVOS, bidSheetFilterDTO.getCurrentPage(),
                bidSheetFilterDTO.getPageSize() == null ? 0 : bidSheetFilterDTO.getPageSize());
    }

    private PageVO<BidSheetStatementVO> queryBidSheetWithStatement_3(BidSheetFilterDTO bidSheetFilterDTO) {
        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }
        List<BidSheetStatementVO> bidSheetStatementVOS = bidSheets.stream().map(bidSheet -> {
            BidSheetStatementVO bidSheetStatementVO = BeanUtil.copyProperties(bidSheet, BidSheetStatementVO.class);
            combineRequestWithStatement(bidSheetStatementVO);
            return bidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(bidSheetStatementVOS, bidSheetFilterDTO.getCurrentPage(),
                bidSheetFilterDTO.getPageSize() == null ? 0 : bidSheetFilterDTO.getPageSize());
    }

    private PageVO<BidSheetStatementVO> queryBidSheetWithStatement_4(BidSheetFilterDTO bidSheetFilterDTO) {

        RequestFilterDTO requestFilterDTO = new RequestFilterDTO();
        requestFilterDTO.setCurrentPage(-1);
        requestFilterDTO.setStatusId(bidSheetFilterDTO.getStatusId());

        //查询出request信息，得到requestIds
        List<Long> requestIds = ieService.findRequestIdsByRequestIdsOrCreatedUsers(requestFilterDTO);
        if (CollectionUtil.isEmpty(requestIds)) {
            return null;
        }

        BidSheetFilterDTO bidSheetFilterDTO1 = new BidSheetFilterDTO();
        bidSheetFilterDTO1.setRequestIds(requestIds);
        bidSheetFilterDTO1.setKeyword(bidSheetFilterDTO.getKeyword());
        bidSheetFilterDTO1.setCreatedUser(bidSheetFilterDTO.getCreatedUser());
        bidSheetFilterDTO1.setProjectSheetId(bidSheetFilterDTO.getProjectSheetId());

        List<BidSheet> bidSheets = queryBidSheetList(bidSheetFilterDTO1);
        if (CollectionUtil.isEmpty(bidSheets)) {
            return null;
        }

        List<BidSheetStatementVO> bidSheetStatementVOS = bidSheets.stream().map(bidSheet -> {
            BidSheetStatementVO bidSheetStatementVO = BeanUtil.copyProperties(bidSheet, BidSheetStatementVO.class);
            combineRequestWithStatement(bidSheetStatementVO);
            return bidSheetStatementVO;
        }).collect(Collectors.toList());

        return PageUtils.getPageVO(bidSheetStatementVOS, bidSheetFilterDTO.getCurrentPage(),
                bidSheetFilterDTO.getPageSize() == null ? 0 : bidSheetFilterDTO.getPageSize());
    }

    private void combineRequestWithStatement(BidSheetStatementVO bidSheetStatementVO) {
        //项目信息
        if (bidSheetStatementVO.getProjectSheetId() != null) {
            ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(bidSheetStatementVO.getProjectSheetId());
            if (ObjectUtil.isNotEmpty(projectSheetVO)) {
                bidSheetStatementVO.setProjectSheetVO(projectSheetVO);
            }
        }

        //表单信息
        if (bidSheetStatementVO.getRequestId() != null) {
            RequestVO requestVO = ieService.findRequestById(bidSheetStatementVO.getRequestId());

            bidSheetStatementVO.setRequestVO(requestVO);
            bidSheetStatementVO.setRequestTitle(requestVO.getTitle());
            bidSheetStatementVO.setOddNumber(requestVO.getOddNumber());
            bidSheetStatementVO.setRequestStatusName(requestVO.getStatus().getName());
        }

        //创建人
        if (bidSheetStatementVO.getCreatedUser() != null) {
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(bidSheetStatementVO.getCreatedUser());
            bidSheetStatementVO.setSysUserInfoVO(sysUserInfoVO);
        }

        bidSheetStatementVO.setRepliesNumber(0L);
        //已回复数量
        BidVendorFilterDTO bidVendorFilterDTO =new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(bidSheetStatementVO.getId());
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);
        if(CollectionUtil.isNotEmpty(bidVendorVOS)){
            //已回复数量
            long repliesNumber = bidVendorVOS.stream().filter(bidVendorVO ->
                    (StrUtil.equals(bidVendorVO.getVendorStatus(),ZtbConstant.VENDOR_REPLIED)
                            ||StrUtil.equals(bidVendorVO.getVendorStatus(),ZtbConstant.VENDOR_AUTHORIZED))).count();
            bidSheetStatementVO.setRepliesNumber(repliesNumber);
            //授权供应商
            List<BidVendorVO> authorizedBidVendorVOS = bidVendorVOS.stream()
                    .filter(bidVendorVO ->StrUtil.equals(bidVendorVO.getVendorStatus(),ZtbConstant.VENDOR_AUTHORIZED))
                    .collect(Collectors.toList());
            if(CollUtil.isNotEmpty(authorizedBidVendorVOS)){
                bidSheetStatementVO.setAuthorizedVendor(authorizedBidVendorVOS.get(NumberConstant.ZERO).getVendorName());
            }
        }



    }

    @Override
    public void exportExcelBidSheetWithStatement(HttpServletResponse response, BidSheetFilterDTO bidSheetFilterDTO) {
        PageVO<BidSheetStatementVO> bidSheetStatementVOPageVO = queryBidSheetWithStatement(bidSheetFilterDTO);
        if(bidSheetStatementVOPageVO ==null || CollectionUtil.isEmpty(bidSheetStatementVOPageVO.getRecords())) {
            return;
        }
        List<BidSheetStatementExcelVO> bidSheetStatementExcelVOS = bidSheetStatementVOPageVO.getRecords().stream().map(bidSheetStatementVO -> {
            BidSheetStatementExcelVO bidSheetStatementExcelVO =new BidSheetStatementExcelVO();
            if(ObjectUtil.isNotEmpty(bidSheetStatementVO.getSysUserInfoVO())){
                bidSheetStatementExcelVO.setCreatedUserName(bidSheetStatementVO.getSysUserInfoVO().getName());
            }
            bidSheetStatementExcelVO.setOddNumber(bidSheetStatementVO.getOddNumber());
            bidSheetStatementExcelVO.setTitle(bidSheetStatementVO.getTitle());
            if(ObjectUtil.isNotEmpty(bidSheetStatementVO.getProjectSheetVO())){
                bidSheetStatementExcelVO.setProjectName(bidSheetStatementVO.getProjectSheetVO().getTitle());
            }
            bidSheetStatementExcelVO.setBidEndTime(DateUtil.formatDateTime(new Date(bidSheetStatementVO.getBidEndTime())));
            bidSheetStatementExcelVO.setRequestStatusName(bidSheetStatementVO.getRequestStatusName());
            bidSheetStatementExcelVO.setRepliesNumber(bidSheetStatementVO.getRepliesNumber());
            bidSheetStatementExcelVO.setAuthorizedVendor(bidSheetStatementVO.getAuthorizedVendor());
            return bidSheetStatementExcelVO;
        }).collect(Collectors.toList());

        try {
            ExportExcelUtil.export(response, BidSheetStatementExcelVO.class, ZtbConstant.BID_STATEMENT, bidSheetStatementExcelVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
