package com.xdcplus.workflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.workflow.common.pojo.entity.UserTo;
import com.xdcplus.workflow.common.pojo.vo.UserToVO;
import com.xdcplus.workflow.mapper.UserToMapper;
import com.xdcplus.workflow.service.HttpService;
import com.xdcplus.workflow.service.UserToService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysDepartmentVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.dto.ToUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  用户去向信息服务实现类
 *
 * @author Rong.Jia
 * @date  2021-07-01
 */
@Slf4j
@Service
public class UserToServiceImpl extends BaseServiceImpl<UserTo, UserToVO, UserTo, UserToMapper> implements UserToService {

    @Autowired
    private UserToMapper userToMapper;

    @Autowired
    private HttpService httpService;

    @Override
    public List<UserToVO> findUserTo() {

        List<UserToVO> userToVOList = this.objectConversion(this.list());

        UserToVO userToVO = userToVOList.stream()
                .filter(a -> ObjectUtil.equal(NumberConstant.FIVE, a.getMark()))
                .findAny().orElse(null);

        if (CollectionUtil.isNotEmpty(userToVOList)) {
            userToVOList.removeIf(a -> ObjectUtil.equal(NumberConstant.FIVE, a.getMark()));
            userToVOList.removeIf(a -> ObjectUtil.equal(NumberConstant.SIX, a.getMark()));
        }

        List<SysDepartmentVO> departmentList = httpService.getDepartments();
        if (ObjectUtil.isNotNull(departmentList) && CollectionUtil.isNotEmpty(userToVOList) && ObjectUtil.isNotNull(userToVO)) {

            List<UserToVO> departmentVOList = departmentList.stream().map(a -> {
                UserToVO b = new UserToVO();
                b.setDescription(a.getFullName() + userToVO.getDescription());
                b.setMark(NumberConstant.FIVE);
                b.setId(a.getId());
                return b;
            }).collect(Collectors.toList());
            userToVOList.addAll(departmentVOList);
        }

        Map<String, Integer> toUsers = ToUserDTO.getToUsers();
        List<UserToVO> toVOList = toUsers.entrySet().stream().map(a -> {
            UserToVO b = new UserToVO();
            b.setDescription(a.getKey());
            b.setId(Convert.toLong(a.getValue()));
            b.setMark(NumberConstant.SIX);
            return b;
        }).collect(Collectors.toList());
        userToVOList.addAll(toVOList);

        return userToVOList;
    }

    @Override
    public UserToVO findUserToByMark(Integer mark) {
        return this.objectConversion(userToMapper.findUserToByMark(mark));
    }
}
