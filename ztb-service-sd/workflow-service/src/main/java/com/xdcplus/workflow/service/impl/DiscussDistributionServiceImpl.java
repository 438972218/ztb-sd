package com.xdcplus.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.workflow.common.pojo.bo.DiscussGroupBO;
import com.xdcplus.workflow.common.pojo.bo.DiscussInfoBO;
import com.xdcplus.workflow.common.pojo.dto.DiscussGroupFilterDTO;
import com.xdcplus.workflow.common.pojo.dto.PostDiscussionsDTO;
import com.xdcplus.workflow.common.pojo.dto.ReplyDiscussDTO;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;
import com.xdcplus.workflow.common.pojo.entity.DiscussRecordRelation;
import com.xdcplus.workflow.common.pojo.vo.DiscussGroupVO;
import com.xdcplus.workflow.common.pojo.vo.DiscussRecordVO;
import com.xdcplus.workflow.mapper.DiscussInfoMapper;
import com.xdcplus.workflow.service.*;
import com.xdcplus.ztb.common.mp.utils.AuthUtils;
import com.xdcplus.ztb.common.tool.constants.AuthConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 讨论分发服务实现
 *
 * @author Rong.Jia
 * @date 2021/08/19 09:44:44
 */
@Service
public class DiscussDistributionServiceImpl implements DiscussDistributionService {

    @Autowired
    private DiscussGroupService discussGroupService;

    @Autowired
    private DiscussRecordService discussRecordService;

    @Autowired
    private DiscussInfoMapper discussInfoMapper;

    @Autowired
    private HttpService httpService;

    @Autowired
    private DiscussGroupRelationService discussGroupRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void postDiscussions(PostDiscussionsDTO postDiscussionsDTO) {

        String currentUser = AuthUtils.getCurrentUser();
        Long group = discussGroupService.saveDiscussGroup(postDiscussionsDTO.getRequestId(), postDiscussionsDTO.getSubject(), currentUser);
        discussGroupRelationService.saveRelation(group, postDiscussionsDTO.getToUsers());
        discussRecordService.saveDiscussRecord(group, postDiscussionsDTO.getContent(), currentUser, postDiscussionsDTO.getToUsers());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void replyDiscuss(ReplyDiscussDTO replyDiscussDTO) {
        discussRecordService.saveDiscussRecord(replyDiscussDTO.getGroupId(), replyDiscussDTO.getContent(), AuthUtils.getCurrentUser(), replyDiscussDTO.getToUsers());
    }

    @Override
    public List<DiscussGroupVO> findDiscussGroup(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        List<DiscussGroupBO> discussGroupList = discussGroupService.findDiscussionGroup(requestId);
        return copyGroupProperties(discussGroupList);
    }

    @Override
    public PageVO<DiscussGroupVO> findDiscussGroup(DiscussGroupFilterDTO discussGroupFilterDTO) {

        PageVO<DiscussGroupVO> pageVO = new PageVO<>();

        if (discussGroupFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(discussGroupFilterDTO);
        }

        List<DiscussGroupBO> discussGroupList = discussGroupService.findDiscussionGroup(discussGroupFilterDTO.getRequestId());
        PageInfo<DiscussGroupBO> pageInfo = new PageInfo<>(discussGroupList);

        PropertyUtils.copyProperties(pageInfo, pageVO, copyGroupProperties(pageInfo.getList()));

        return pageVO;
    }

    @Override
    public List<DiscussRecordVO> findDiscussRecordsByGroupId(Long groupId) {

        Assert.notNull(groupId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<DiscussInfoBO> discussInfoBOList = discussInfoMapper.findDiscussInfoByGroupId(groupId);
        return copyProperties(discussInfoBOList);
    }

    @Override
    public List<DiscussRecordVO> findDiscussRecordsByRequestId(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<DiscussInfoBO> discussInfoBOList = discussInfoMapper.findDiscussInfoByRequestId(requestId);
        return copyProperties(discussInfoBOList);
    }

    /**
     * 复制记录属性
     *
     * @param discussRecordBOList 讨论记录
     * @return {@link List<DiscussRecordVO>}
     */
    private List<DiscussRecordVO> copyProperties(List<DiscussInfoBO> discussRecordBOList) {

        if (CollectionUtil.isNotEmpty(discussRecordBOList)) {
            List<DiscussRecordVO> discussRecordVOList = new ArrayList<>();
            List<String> subUsers = httpService.getSubordinateUserNames(AuthUtils.getCurrentUser());
            Map<String, String> allUsers = httpService.getAllUsers();
            for (DiscussInfoBO discussInfoBO : discussRecordBOList) {
                if (isSkip(discussInfoBO, subUsers)) {
                    DiscussRecordVO discussRecordVO = this.copyProperties(discussInfoBO, allUsers, subUsers);
                    Optional.ofNullable(discussRecordVO).ifPresent(discussRecordVOList::add);
                }
            }

            return discussRecordVOList;
        }

        return null;
    }

    /**
     * 复制记录属性
     *
     * @param discussGroupBOList 讨论记录
     * @return {@link List<DiscussRecordVO>}
     */
    private List<DiscussGroupVO> copyGroupProperties(List<DiscussGroupBO> discussGroupBOList) {

        if (CollectionUtil.isNotEmpty(discussGroupBOList)) {
            List<DiscussGroupVO> discussRecordVOList = new ArrayList<>();
            List<String> subUsers = httpService.getSubordinateUserNames(AuthUtils.getCurrentUser());
            for (DiscussGroupBO discussGroupBO : discussGroupBOList) {
                if (isSkip(discussGroupBO, subUsers)) {
                    DiscussGroupVO discussGroupVO = discussGroupService.objectConversion(discussGroupBO);
                    Optional.ofNullable(discussGroupVO).ifPresent(discussRecordVOList::add);
                }
            }

            return discussRecordVOList;
        }

        return null;
    }


    /**
     * 复制属性
     *
     * @param discussInfoBO 讨论记录BO
     * @param subUsers 子用户
     * @return {@link DiscussRecordVO}
     */
    private DiscussRecordVO copyProperties(DiscussInfoBO discussInfoBO, Map<String, String> allUsers, List<String> subUsers) {

        if (ObjectUtil.isNotNull(discussInfoBO)) {
            DiscussRecordVO discussRecordVO = new DiscussRecordVO();
            BeanUtil.copyProperties(discussInfoBO, discussRecordVO);
            replaceUser(discussInfoBO, discussRecordVO, allUsers);
           Optional.ofNullable(discussInfoBO.getGroup()).ifPresent(a -> discussRecordVO.setDiscussGroup(discussGroupService.objectConversion(a)));
            return discussRecordVO;
        }

        return null;
    }

    /**
     * 是否跳过当条数据
     *
     * @param discussInfoBO 讨论信息BO
     * @param subUsers 子用户集
     * @return {@link Boolean} false:跳过 ，true: 不跳过
     */
    private Boolean isSkip(DiscussInfoBO discussInfoBO, List<String> subUsers) {
        String currentUser = AuthUtils.getCurrentUser();
        if (!StrUtil.equals(AuthConstant.ADMINISTRATOR, currentUser)) {
            Set<String> toUsers = discussInfoBO.getRelations().stream().map(DiscussRecordRelation::getToUser).collect(Collectors.toSet());
            if (toUsers.stream().noneMatch(a -> StrUtil.equals(Convert.toStr(NumberConstant.A_NEGATIVE), a))) {
                String fromUser = discussInfoBO.getFromUser();
                boolean flag = Boolean.FALSE;
                if (StrUtil.equals(currentUser, fromUser) || toUsers.contains(currentUser)) {
                    flag = Boolean.TRUE;
                }

                if (CollectionUtil.isNotEmpty(subUsers)) {
                    for (String subUser : subUsers) {
                        flag = flag || toUsers.contains(subUser);
                    }
                }
                return flag;
            }

        }

        return Boolean.FALSE;
    }

    /**
     * 是否跳过当条数据
     *
     * @param discussGroupBO 讨论组信息BO
     * @param subUsers 子用户集
     * @return {@link Boolean} false:跳过 ，true: 不跳过
     */
    private Boolean isSkip(DiscussGroupBO discussGroupBO, List<String> subUsers) {
        String currentUser = AuthUtils.getCurrentUser();
        if (!StrUtil.equals(AuthConstant.ADMINISTRATOR, currentUser)) {
            Set<String> toUsers = discussGroupBO.getRelations().stream().map(DiscussGroupRelation::getToUser).collect(Collectors.toSet());
            if (toUsers.stream().noneMatch(a -> StrUtil.equals(Convert.toStr(NumberConstant.A_NEGATIVE), a))) {
                String createdUser = discussGroupBO.getCreatedUser();
                boolean flag = Boolean.FALSE;
                if (StrUtil.equals(currentUser, createdUser) || toUsers.contains(currentUser)) {
                    flag = Boolean.TRUE;
                }
                if (CollectionUtil.isNotEmpty(subUsers)) {
                    for (String subUser : subUsers) {
                        flag = flag || toUsers.contains(subUser);
                    }
                }
                return flag;
            }
        }

        return Boolean.FALSE;
    }


    /**
     * 替代用户
     * @param allUsers 用户信息
     * @param discussInfoBO 讨论记录
     * @param discussRecordVO 讨论记录VO
     */
    private void replaceUser(DiscussInfoBO discussInfoBO, DiscussRecordVO discussRecordVO, Map<String, String> allUsers) {

        if (ObjectUtil.isAllNotEmpty(discussInfoBO, discussRecordVO)) {
            if (CollectionUtil.isNotEmpty(allUsers)) {
                String fromUser = discussInfoBO.getFromUser();
                if (allUsers.containsKey(fromUser)) {
                    discussRecordVO.setFrom(DiscussRecordVO.CommunicationPersonBean.builder().account(fromUser).name(allUsers.get(fromUser)).build());
                }

                List<DiscussRecordRelation> relations = discussInfoBO.getRelations();
                if (CollectionUtil.isNotEmpty(relations)) {
                    Set<String> toUsers = relations.stream().map(DiscussRecordRelation::getToUser).collect(Collectors.toSet());
                    if (toUsers.stream().noneMatch(a -> StrUtil.equals(Convert.toStr(NumberConstant.A_NEGATIVE), a))) {
                        List<DiscussRecordVO.CommunicationPersonBean> communicationPersonBeans = toUsers.stream().map(a -> {
                            if (allUsers.containsKey(a)) {
                                return DiscussRecordVO.CommunicationPersonBean.builder().account(a).name(allUsers.get(a)).build();
                            }
                            return null;
                        }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
                        discussRecordVO.setTos(communicationPersonBeans);
                    }
                }
            }
        }
    }










}
