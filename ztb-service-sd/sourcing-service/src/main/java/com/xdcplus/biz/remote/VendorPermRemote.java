package com.xdcplus.biz.remote;

import com.xdcplus.biz.remote.fallback.PermRemoteFallbackFactory;
import com.xdcplus.ztb.common.remote.domain.perm.vo.*;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 烫用户远程
 *
 * @author Fish.Fei
 * @date 2021/08/18
 */
@Component
@RequestMapping("apivendor")
@FeignClient(value = ServiceConstant.VENDOR_PERM_SERVICE, fallbackFactory = PermRemoteFallbackFactory.class)
public interface VendorPermRemote {

    @GetMapping(value = "queryByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> queryByUserName(@PathVariable("userName") String userName);
    /**
     * 根据用户名查询员工信息
     */
    @PostMapping(value = "getEmployeeVoByUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysEmployeeVO> getEmployeeVoByUserName(@PathVariable("userName") String userName);

    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    @GetMapping(value = "queryRoleByUserId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<List<SysRoleVO>> queryByUserId(@PathVariable("userId") Long userId);

    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link SysUserInfoVO} 用户信息vo
     */
    @GetMapping(value = "queryUserById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<SysUserInfoVO> sysUserQueryById(@PathVariable("id") Long id);

}
