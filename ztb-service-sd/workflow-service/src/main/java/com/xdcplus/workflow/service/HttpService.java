package com.xdcplus.workflow.service;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysDepartmentVO;

import java.util.List;
import java.util.Map;

/**
 * HTTP 调用业务层接口
 *
 * @author Rong.Jia
 * @date 2021/07/20
 */
public interface HttpService {

    /**
     *  获取总经理用户标识
     * @return 总经理用户标识
     */
    Long getBigBossUser();

    /**
     * 根据用户标识/用户名获取上级的用户标识
     * @param userId 用户标识
     * @param username 用户名
     * @return 上级的用户标识
     */
    Long getSuperiorUserByUser(Long userId, String username);

    /**
     * 获取部门信息
     * @return 部门信息
     */
    List<SysDepartmentVO> getDepartments();

    /**
     * 根据部门标识获取部门负责人标识
     * @param departmentId 部门标识
     * @return 部门负责人标识
     */
    Long getDepartmentHeadByDepartmentId(Long departmentId);

    /**
     * 根据用户名获取用户标识
     * @param username 用户名
     * @return 用户标识
     */
    Long getUserInfoByName(String username);

    /**
     * 刷新部门缓存
     */
    void refreshDepartmentsCache();

    /**
     * 根据自定义去向的用户ID
     *
     * @param mark      标识
     * @param requestId 表单ID
     * @return {@link Long} 去向的用户ID
     */
    Long getCustomToUserId(String mark, Long requestId);

    /**
     *  根据账号获取子级账号
     * @param userName 账号
     * @return 子级账号
     */
    List<String> getSubordinateUserNames(String userName);

    /**
     * 获取所有用户
     *
     * @return {@link Map}<{@link String}, {@link String}>  key: 账号,value: 名称
     */
    Map<String, String> getAllUsers();












}
