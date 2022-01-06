package com.xdcplus.vendor.remote;

import com.xdcplus.vendor.remote.fallback.InteriorPermRemoteFallbackFactory;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


/**
 * 权限收放调用
 *
 * @author Rong.Jia
 * @date 2021/08/03 16:21:01
 */
@Component
@RequestMapping("/api")
@FeignClient(value = ServiceConstant.PERM_SERVICE, fallbackFactory = InteriorPermRemoteFallbackFactory.class)
public interface InteriorPermRemote {

    @PostMapping(value = "registerUser", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> registerUser(@Validated @RequestBody RegisterUserDTO registerUserDto);

    @GetMapping(value = "getSysRoleMarkByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<String> getSysRoleMarkByUserName(@PathVariable("userName") String userName);

    @GetMapping(value = "queryByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> queryByUserName(@PathVariable("userName")
                                              @NotNull(message = "userName不能为空") String userName);
    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link SysUserInfoVO} 用户信息vo
     */
    @GetMapping(value = "queryUserById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> sysUserQueryById(@PathVariable("id") Long id);

    /**
     * 新增用户角色信息
     *
     * @param sysUserRole 用户角色信息
     * @return {@link ResponseVO}
     */
    @PostMapping(value = "insertUserRole", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO insertUserRole(@RequestBody SysUserRoleInfoDTO sysUserRole);

}
