package com.xdcplus.vendor.remote;

import com.xdcplus.vendor.remote.fallback.PermRemoteFallbackFactory;
import com.xdcplus.ztb.common.mp.utils.AuthUtils;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 权限收放调用
 *
 * @author Rong.Jia
 * @date 2021/08/03 16:21:01
 */
@Component
@RequestMapping("/apivendor")
@FeignClient(value = ServiceConstant.VENDOR_PERM_SERVICE, fallbackFactory = PermRemoteFallbackFactory.class)
public interface PermRemote {

    @PostMapping(value = "registerUser", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> registerUser(@Validated @RequestBody RegisterUserDTO registerUserDto);

    /**
     * 注册用户(不发邮件通知)
     *
     * @param registerUserDto 注册用户dto
     * @return {@link ResponseVO<SysUserInfoVO>}
     */
    @PostMapping(value = "registerUserNotSendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> registerUserNotSendEmail(@Validated @RequestBody RegisterUserDTO registerUserDto);

    /**
     * 注册用户(覆盖原始数据)
     *
     * @param registerUserDto 注册用户dto
     * @return {@link ResponseVO<SysUserInfoVO>}
     */
    @ApiOperation("注册用户(覆盖原始数据)")
    @PostMapping(value = "registerUserCover", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> registerUserCover(@Validated @RequestBody RegisterUserDTO registerUserDto);

    /**
     * 注册用户(不发邮件,覆盖原始数据)
     *
     * @param registerUserDto 注册用户dto
     * @return {@link ResponseVO<SysUserInfoVO>}
     */
    @ApiOperation("注册用户(不发邮件,覆盖原始数据)")
    @PostMapping(value = "registerUserNotSendEmailAndCover", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> registerUserNotSendEmailAndCover(@Validated @RequestBody RegisterUserDTO registerUserDto);

    @GetMapping(value = "getSysRoleMarkByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<String> getSysRoleMarkByUserName(@PathVariable("userName") String userName);

    @GetMapping(value = "queryByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> queryByUserName(@PathVariable("userName")
                                                 @NotNull(message = "userName不能为空")String userName);
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
    ResponseVO insertUserRole (@RequestBody SysUserRoleInfoDTO sysUserRole);

    /**
     * 查询出所有的用户
     *
     */
    @GetMapping(value = "queryAllVendorUser", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<List<SysUserInfoVO>> queryAllVendorUser();

    /**
     * 查询用户
     *
     */
    @PostMapping(value = "queryVendorUser", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<PageVO<SysUserInfoVO>> querySysUserVOPageVO(@RequestBody SysUserInfoDTO sysUserInfoDTO);

}
