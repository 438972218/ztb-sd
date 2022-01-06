package com.xdcplus.statement.remote.fallback;

import com.xdcplus.statement.remote.PermRemote;
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
public class PermRemoteFallbackFactory implements FallbackFactory<PermRemote> {

    @Override
    public PermRemote create(Throwable throwable) {
        return new PermRemote() {

            @Override
            public ResponseVO<SysUserInfoVO> sysUserQueryById(Long id) {
                log.error("sysUserQueryById {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<List<String>> getSubordinateUserNames(String userName) {
                log.error("getSubordinateUserNames {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<List<SysUserInfoVO>> getSysUserByRoleId(Long roleId) {
                log.error("getSysUserByRoleId {}", throwable.getMessage());
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
            public ResponseVO<SysCompanyVO> sysCompanyQueryById(Long id) {
                log.error("sysCompanyQueryById {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<List<SysCompanyVO>> getDepartmentTree() {
                log.error("getDepartmentTree {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO judgeGroupCompany(Long id) {
                log.error("judgeGroupCompany {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<List<SysRoleVO>> queryByUserId(Long userId) {
                log.error("queryByUserId {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO> getDepartmentManagerEmployeeVoAndSysUserVoByUserName(String userName) {
                log.error("getDepartmentManagerEmployeeVoAndSysUserVoByUserName {}", throwable.getMessage());
                return ResponseVO.error();
            }
        };
    }










}
