package com.xdcplus.statement.remote.fallback;

import com.xdcplus.statement.remote.VendorPermRemote;
import com.xdcplus.ztb.common.remote.domain.perm.vo.*;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 烫远程后备工厂
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
@Slf4j
@Component
public class VendorPermRemoteFallbackFactory implements FallbackFactory<VendorPermRemote> {

    @Override
    public VendorPermRemote create(Throwable throwable) {
        return new VendorPermRemote() {

            @Override
            public ResponseVO<SysUserInfoVO> sysUserQueryById(Long id) {
                log.error("sysUserQueryById {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<SysUserInfoVO> queryByUserName(String userName) {
                log.error("queryByUserName {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<SysEmployeeVO> getEmployeeVoByUserName(String userName) {
                log.error("getEmployeeVoByUserName {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<List<SysRoleVO>> queryByUserId(Long userId) {
                log.error("queryByUserId {}", throwable.getMessage());
                return ResponseVO.error();
            }

        };
    }










}
