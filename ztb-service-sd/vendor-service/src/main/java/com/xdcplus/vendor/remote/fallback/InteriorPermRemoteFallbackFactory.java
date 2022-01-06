package com.xdcplus.vendor.remote.fallback;

import com.xdcplus.vendor.remote.InteriorPermRemote;
import com.xdcplus.vendor.remote.PermRemote;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 权限收放降级处理
 *
 * @author Rong.Jia
 * @date 2021/08/03 16:27:47
 */
@Slf4j
@Component
public class InteriorPermRemoteFallbackFactory implements FallbackFactory<InteriorPermRemote> {

    @Override
    public InteriorPermRemote create(Throwable cause) {
        return new InteriorPermRemote() {
            @Override
            public ResponseVO registerUser(RegisterUserDTO registerUserDto) {
                log.error("registerUser {}", cause.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<String> getSysRoleMarkByUserName(String userName) {
                log.error("getSysRoleMarkByUserName {}", cause.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<SysUserInfoVO> queryByUserName(@NotNull(message = "userName不能为空") String userName) {
                log.error("queryByUserName {}", cause.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<SysUserInfoVO> sysUserQueryById(Long id) {
                log.error("sysUserQueryById {}", cause.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO insertUserRole(SysUserRoleInfoDTO sysUserRole) {
                log.error("insertUserRole {}", cause.getMessage());
                return ResponseVO.error();            }
        };
    }










}
