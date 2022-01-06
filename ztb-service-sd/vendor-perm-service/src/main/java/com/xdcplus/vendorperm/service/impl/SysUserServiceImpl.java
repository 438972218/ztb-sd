package com.xdcplus.vendorperm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendorperm.common.enums.IsDeletedEnum;
import com.xdcplus.vendorperm.common.pojo.dto.sysuser.*;
import com.xdcplus.vendorperm.common.pojo.entity.SysUser;
import com.xdcplus.vendorperm.common.pojo.entity.SysUserRole;
import com.xdcplus.vendorperm.common.pojo.query.sysuser.SysUserFilterQuery;
import com.xdcplus.vendorperm.common.pojo.vo.sysPermission.SysPermissionVo;
import com.xdcplus.vendorperm.common.pojo.vo.sysdepartment.SysDepartmentVo;
import com.xdcplus.vendorperm.common.pojo.vo.sysemployee.SysEmployeeVo;
import com.xdcplus.vendorperm.common.pojo.vo.sysrole.SysRoleVo;
import com.xdcplus.vendorperm.common.pojo.vo.sysuser.SysUserVo;
import com.xdcplus.vendorperm.common.pojo.vo.sysuser.UserPermVO;
import com.xdcplus.vendorperm.dao.SysUserDao;
import com.xdcplus.vendorperm.remote.service.WorkFlowService;
import com.xdcplus.vendorperm.service.*;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.MailBeanInfo;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息表(SysUser)表服务实现类
 *
 * @author Bullion.Yan
 * @since 2021-06-28 13:10:10
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl
        extends BaseServiceImpl<SysUser, SysUserVo, SysUser, SysUserDao>
        implements SysUserService {
    @Resource
    private SysUserDao sysUserDao;

    @Autowired
    private SysEmployeeService sysEmployeeService;


    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public PageVO<SysUserVo> getSysUserServicePageByCondition(SysUserFilterDto sysUserFilterDto) {
        PageVO<SysUserVo> pageVo = new PageVO<>();
        //1.设置查询对象
        SysUserFilterQuery sysUserFilterQuery = new SysUserFilterQuery();
        //2.设置分页
        PageableUtils.basicPage(sysUserFilterDto);
        //3.将dto对象转换为query对象
        org.springframework.beans.BeanUtils.copyProperties(sysUserFilterDto, sysUserFilterQuery);
        //4.查询
        List<SysUser> sysUserList = sysUserDao.getSysUserByCondition(sysUserFilterQuery);
        PageInfo pageInfo = new PageInfo<>(sysUserList);
        PropertyUtils.copyProperties(pageInfo, pageVo);
        pageVo.setRecords(this.objectConversion(sysUserList));
        if (pageVo.getRecords() != null) {
            for (SysUserVo sysUserVo : pageVo.getRecords()) {
                //4.1 根据角色获取所绑定的权限组
                List<SysUserRole> SysUserRoleList = sysUserRoleService.getByUserId(sysUserVo.getId());
                if (SysUserRoleList != null && SysUserRoleList.size() > 0) {
                    List<Long> roleIdList = SysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    sysUserVo.setSysRoleVoList(roleIdList);
                }
            }
        }

        return pageVo;
    }

    @Override
    public SysUserVo getSysUserManagerByDepartmentId(Long departmentId) {
        SysDepartmentVo sysDepartmentVo = sysDepartmentService.queryById(departmentId);
        if (ObjectUtil.isEmpty(sysDepartmentVo)) {
            log.error("departmentId不存在" + departmentId);
            throw new ZtbWebException(ResponseEnum.DEPARTMENT_ID_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DEPARTMENT_ID_IS_NOT_EXISTS.getMessage());
        }
        if (sysDepartmentVo.getManager() == null) {
            log.error("部门负责没有维护" + departmentId);
            throw new ZtbWebException(ResponseEnum.DEPARTMENT_MANAGER_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DEPARTMENT_MANAGER_IS_NOT_EXISTS.getMessage());
        }
        SysUser sysUser = sysUserDao.getSysUserByEmployeeId(sysDepartmentVo.getManager());
        return this.objectConversion(sysUser);
    }

    @Override
    public SysUserVo queryById(Long id) {
        SysUser sysUser = this.getById(id);
        if (sysUser != null && IsDeletedEnum.NO_DELETED.getCode().equals(sysUser.getDeleted())) {
            SysUserVo sysUserVo = this.objectConversion(sysUser);
            return sysUserVo;
        }
        return null;
    }

    @Override
    public Map<String, String> queryMap() {
        SysUserFilterQuery sysUserFilterQuery = new SysUserFilterQuery();
        List<SysUser> sysUsers = sysUserDao.getSysUserByCondition(sysUserFilterQuery);
        if (CollectionUtil.isEmpty(sysUsers)) {
            return null;
        }
        return sysUsers.stream().collect(Collectors.toMap(SysUser::getUserName, SysUser::getName));
    }

    @Override
    public SysUserVo queryByUserName(String userName) {
        SysUser sysUser = this.sysUserDao.getSysUserByUserName(userName);
        if (sysUser != null && IsDeletedEnum.NO_DELETED.getCode().equals(sysUser.getDeleted())) {
            SysUserVo sysUserVo = this.objectConversion(sysUser);
            return sysUserVo;
        }
        return null;
    }

    @Override
    public SysUserVo getSysUserByUserIdOrUserName(GetSysUserByUserIdOrUserNameDto getSysUserByUserIdOrUserNameDto) {
        //1.验证请求参数的有效性
        if (getSysUserByUserIdOrUserNameDto == null) {
            throw new ZtbWebException(ResponseEnum.REQUEST_PARAMETER_IS_NULL);
        }
        if (StringUtils.isBlank(getSysUserByUserIdOrUserNameDto.getUserName()) && getSysUserByUserIdOrUserNameDto.getId() == null) {
            throw new ZtbWebException(ResponseEnum.USER_NAME_AND_USER_ID_IS_ALL_NULL.getCode(),
                    ResponseEnum.USER_NAME_AND_USER_ID_IS_ALL_NULL.getMessage());
        }
        //2.根据用户名或者用户id查询用户信息
        SysUser sysUser = sysUserDao.getSysUserByUserIdOrUserName(getSysUserByUserIdOrUserNameDto.getUserName(), getSysUserByUserIdOrUserNameDto.getId());
        if (sysUser == null) {
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        //3.验证用户信息是否绑定员工id
        if (sysUser.getEmployeeId() == null) {
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EMPLOYEE_ID.getCode(),
                    ResponseEnum.USER_IS_NOT_EMPLOYEE_ID.getMessage());
        }
        //4.根据员工id获取员工记录信息
        SysEmployeeVo sysEmployeeVo = sysEmployeeService.queryById(sysUser.getEmployeeId());
        if (sysEmployeeVo == null) {
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getMessage());
        }
        //6.根据员工的直接上级查询直接上级的用户及员工信息
        if (sysEmployeeVo.getManagerId() == null) {
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getMessage());
        }
        SysEmployeeVo managerIdVo = sysEmployeeService.queryById(sysEmployeeVo.getManagerId());
        if (managerIdVo == null) {
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getMessage());
        }
        SysUser sysMangerUser = sysUserDao.getSysUserByEmployeeId(managerIdVo.getId());
        return this.objectConversion(sysMangerUser);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insert(SysUserDto sysUserDto, String loginUser) {
        //1.验证用户名是否存在
        insertValidate(sysUserDto);
        //2.保存用户
        SysUser sysUser = new SysUser();
        org.springframework.beans.BeanUtils.copyProperties(sysUserDto, sysUser);
        long currentTime = DateUtil.current();
        sysUser.setPassword(DigestUtil.bcrypt(sysUserDto.getPassword()));
        sysUser.setStatus("0");//正常
        sysUser.setLockStatus("0");
        sysUser.setUpdatedTime(currentTime);
        sysUser.setCreatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setCreatedUser(loginUser);
        sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
        this.save(sysUser);
        //3.保存用户角色绑定关系
        saveUserRole(sysUserDto, sysUser, loginUser);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SysUserVo registerUser(RegisterUserDto registerUserDto) {
        //1.验证用户名是否存在
        SysUser sysUserNameExists = sysUserDao.getSysUserByUserNameAndNoId(registerUserDto.getUserName(), null);
        if (sysUserNameExists != null) {
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //2.验证邮箱是否存在
//        SysUser sysUserEmailExists=sysUserDao.getSysUserByEmail(registerUserDto.getMail());
//        if(sysUserEmailExists!=null && (!registerUserDto.getUserName().equals(sysUserEmailExists.getUserName()))){
//            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getCode(),
//                    ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getMessage());
//        }
        //2.保存用户
        SysUser sysUser = new SysUser();
        org.springframework.beans.BeanUtils.copyProperties(registerUserDto, sysUser);
        long currentTime = DateUtil.current();
        sysUser.setPassword(DigestUtil.bcrypt(registerUserDto.getPassword()));
        sysUser.setStatus("0");//正常
        sysUser.setLockStatus("0");
        sysUser.setCreatedTime(currentTime);
        sysUser.setCreatedUser("用户注册");
        sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
        this.save(sysUser);
        SysUserVo sysUserVo = this.objectConversion(sysUser);
        //3.注册用户，发送邮件
        registerUserSendEmail(sysUserVo.getName(), sysUserVo.getUserName(), sysUserVo.getMail(), registerUserDto.getPassword());

        return sysUserVo;
    }

    @Override
    public SysUserVo registerUserNotSendEmail(RegisterUserDto registerUserDto) {
        //1.验证用户名是否存在
        SysUser sysUserNameExists = sysUserDao.getSysUserByUserNameAndNoId(registerUserDto.getUserName(), null);
        if (sysUserNameExists != null) {
            return this.objectConversion(sysUserNameExists);
        }
        //2.验证邮箱是否存在
//        SysUser sysUserEmailExists=sysUserDao.getSysUserByEmail(registerUserDto.getMail());
//        if(sysUserEmailExists!=null && (!registerUserDto.getUserName().equals(sysUserEmailExists.getUserName()))){
//            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getCode(),
//                    ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getMessage());
//        }
        //2.保存用户
        SysUser sysUser = new SysUser();
        org.springframework.beans.BeanUtils.copyProperties(registerUserDto, sysUser);
        long currentTime = DateUtil.current();
        sysUser.setPassword(DigestUtil.bcrypt(registerUserDto.getPassword()));
        sysUser.setStatus("0");//正常
        sysUser.setLockStatus("0");
        sysUser.setCreatedTime(currentTime);
        sysUser.setCreatedUser("用户注册");
        sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
        this.save(sysUser);
        //3.注册用户，发送邮件
//        registerUserSendEmail(sysUser.getName(),sysUser.getUserName(),sysUser.getMail(),registerUserDto.getPassword());
        return this.objectConversion(sysUser);
    }

    @Override
    public SysUserVo registerUserNotSendEmailAndCover(RegisterUserDto registerUserDto) {
        //1.验证用户名是否存在
        SysUser sysUserNameExists = sysUserDao.getSysUserByUserNameAndNoId(registerUserDto.getUserName(), null);
        //2.验证邮箱是否存在
//        SysUser sysUserEmailExists=sysUserDao.getSysUserByEmail(registerUserDto.getMail());
//        if(sysUserEmailExists!=null && (!registerUserDto.getUserName().equals(sysUserEmailExists.getUserName()))){
//            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getCode(),
//                    ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getMessage());
//        }
        SysUser sysUser;
        if (sysUserNameExists != null) {
            //覆盖数据
            //2.修改用户
            sysUser = new SysUser();
            org.springframework.beans.BeanUtils.copyProperties(registerUserDto, sysUser);
            long currentTime = DateUtil.current();
            sysUser.setPassword(DigestUtil.bcrypt(registerUserDto.getPassword()));
            sysUser.setStatus("0");//正常
            sysUser.setLockStatus("0");
            sysUser.setCreatedTime(currentTime);
            sysUser.setCreatedUser("用户注册");
            sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
            this.update(sysUser, new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, sysUserNameExists.getId()));
        } else {
            //2.保存用户
            sysUser = new SysUser();
            org.springframework.beans.BeanUtils.copyProperties(registerUserDto, sysUser);
            long currentTime = DateUtil.current();
            sysUser.setPassword(DigestUtil.bcrypt(registerUserDto.getPassword()));
            sysUser.setStatus("0");//正常
            sysUser.setLockStatus("0");
            sysUser.setCreatedTime(currentTime);
            sysUser.setCreatedUser("用户注册");
            sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
            this.save(sysUser);
            //3.注册用户，发送邮件
//        registerUserSendEmail(sysUser.getName(),sysUser.getUserName(),sysUser.getMail(),registerUserDto.getPassword());
        }
        return this.objectConversion(sysUser);

    }

    @Override
    public SysUserVo registerUserCover(RegisterUserDto registerUserDto) {
        SysUserVo sysUserVo = registerUserNotSendEmailAndCover(registerUserDto);
        //3.注册用户，发送邮件
        registerUserSendEmail(sysUserVo.getName(), sysUserVo.getUserName(), sysUserVo.getMail(), registerUserDto.getPassword());

        return sysUserVo;
    }

    /**
     * 注册用户，发送邮件
     *
     * @param userName 用户名
     * @param mail     邮件
     */
    private void registerUserSendEmail(String name, String userName, String mail, String password) {
        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.ONE));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(mail);
        mailBeanInfo.setName(name);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));

//        extraMailNotificationDTO.setRequestId(132132132L);
//        extraMailNotificationDTO.setFlowId(132132132L);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        objectMap.put("homepage", "https://esourcing.suntech-power.com.cn");
        objectMap.put("userName", userName);
        objectMap.put("password", password);

        extraMailNotificationInfoDTO.setExtra(objectMap);

        log.info("发送邮件请求报文：{}", JSON.toJSONString(extraMailNotificationInfoDTO));

        workFlowService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 插入验证
     *
     * @param sysUserDto 系统用户dto
     */
    private void insertValidate(SysUserDto sysUserDto) {
        //1.验证用户名是否存在
        SysUser sysUserNameExists = sysUserDao.getSysUserByUserNameAndNoId(sysUserDto.getUserName(), null);
        if (sysUserNameExists != null) {
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //2.验证员工是否存在的
        SysEmployeeVo sysEmployeeVo = sysEmployeeService.queryById(sysUserDto.getEmployeeId());
        if (sysEmployeeVo == null) {
            throw new ZtbWebException(ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getMessage());
        }
        //3.检查员工是否被其他用户绑定了
        List<SysUser> sysUserByEmployeeIdList = sysUserDao.getSysUserByEmployeeAndNoId(sysUserDto.getEmployeeId(), null);
        if (sysUserByEmployeeIdList != null && sysUserByEmployeeIdList.size() > 0) {
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_Bind.getCode(),
                    ResponseEnum.EMPLOYEE_IS_Bind.getMessage());
        }
        //4.绑定角色
        if (sysUserDto.getSysRoleVoList() == null || sysUserDto.getSysRoleVoList().size() == 0) {
            throw new ZtbWebException(ResponseEnum.USER_ROLE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.USER_ROLE_IS_NOT_EXISTS.getMessage());
        }
    }

    /**
     * 保存用户角色绑定关系
     *
     * @param sysUserDto
     * @param sysUser
     */
    private void saveUserRole(SysUserDto sysUserDto, SysUser sysUser, String loginUser) {
        for (Long roleId : sysUserDto.getSysRoleVoList()) {
            if (roleId != null) {
                //检验role_id是否存在
                SysRoleVo sysRoleVo = sysRoleService.queryById(roleId);
                if (sysRoleVo == null) {
                    throw new ZtbWebException(ResponseEnum.USER_ROLE_BIND_ERROR.getCode(),
                            ResponseEnum.USER_ROLE_BIND_ERROR.getMessage());
                }
            }
            long currentTime = DateUtil.current();
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreatedTime(currentTime);
            sysUserRole.setCreatedUser(loginUser);
            sysUserRoleService.insert(sysUserRole);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(SysUserDto sysUserDto, String loginUser) {
        //1.更新验证
        SysUser sysUser = updateByIdValidate(sysUserDto);
        //2.绑定用户角色
        //2.1删除用户之前绑定的角色
        sysUserRoleService.deleteByUserId(sysUser.getId());
        //2.2保存新选择的用户角色
        saveUserRole(sysUserDto, sysUser, loginUser);
        org.springframework.beans.BeanUtils.copyProperties(sysUserDto, sysUser);
        long currentTime = DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setPassword(null);//更新用户，不允许更新密码;设置为空，主要是避免前台不小心传过来密码，后台更新了前台传密码过来,影响到数据 yanjinyin
        //3.更新用户
        this.updateById(sysUser);
    }

    @Override
    public void updatePasswordById(UpdatePasswordByIdDto updatePasswordByIdDto, String loginUser) {
        //1.检查id是否存在
        SysUser sysUser = this.getById(updatePasswordByIdDto.getId());
        if (sysUser == null) {
            log.error("用户id不存在:" + updatePasswordByIdDto.toString());
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        if (!DigestUtil.bcryptCheck(updatePasswordByIdDto.getOldPassword(), sysUser.getPassword())) {
            log.error("用户旧的密码不正确:" + updatePasswordByIdDto.toString());
            throw new ZtbWebException(ResponseEnum.USER_OLD_PASSWORD_IS_ERROR.getCode(),
                    ResponseEnum.USER_OLD_PASSWORD_IS_ERROR.getMessage());
        }

        sysUser.setPassword(DigestUtil.bcrypt(updatePasswordByIdDto.getNewPassword()));
        long currentTime = DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        //更新用户
        this.updateById(sysUser);

        updateUserSendEmail(sysUser.getName(), sysUser.getMail());
    }

    @Override
    public void updateMailById(SysUserDto sysUserDto, String loginUser) {
        //1.检查id是否存在
        SysUser sysUser = this.getById(sysUserDto.getId());
        if (sysUser == null) {
            log.error("用户id不存在:" + sysUserDto.toString());
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }

        sysUser.setMail(sysUserDto.getMail());
        long currentTime = DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        //更新用户
        this.updateById(sysUser);
    }


    /**
     * 修改用户，发送邮件
     *
     * @param mail 邮件
     */
    private void updateUserSendEmail(String name, String mail) {
        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.FIVE));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(mail);
        mailBeanInfo.setName(name);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));


        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("time", DateUtil.date());

        extraMailNotificationInfoDTO.setExtra(objectMap);

        log.info("发送邮件请求报文：{}", JSON.toJSONString(extraMailNotificationInfoDTO));

        workFlowService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 通过id更新验证数据有效性
     *
     * @param sysUserDto 系统用户dto
     * @return {@link SysUser}
     */
    private SysUser updateByIdValidate(SysUserDto sysUserDto) {
        //1.检查id是否存在
        SysUser sysUser = this.getById(sysUserDto.getId());
        if (sysUser == null) {
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        //2.验证用户名是否存在
        SysUser sysUserNameExists = sysUserDao.getSysUserByUserNameAndNoId(sysUserDto.getUserName(), sysUserDto.getId());
        if (sysUserNameExists != null) {
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //3.验证员工是否存在的
        SysEmployeeVo sysEmployeeVo = sysEmployeeService.queryById(sysUserDto.getEmployeeId());
        if (sysEmployeeVo == null) {
            throw new ZtbWebException(ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getMessage());
        }
        //5.检查员工是否被其他用户绑定了
        List<SysUser> sysUserByEmployeeIdList = sysUserDao.getSysUserByEmployeeAndNoId(sysUser.getEmployeeId(), sysUserDto.getId());
        if (sysUserByEmployeeIdList != null && sysUserByEmployeeIdList.size() > 0) {
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_Bind.getCode(),
                    ResponseEnum.EMPLOYEE_IS_Bind.getMessage());
        }
        return sysUser;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long id, String loginUser) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        long currentTime = DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setDeleted(IsDeletedEnum.DELETED.getCode());//删除
        sysUserRoleService.deleteByUserId(sysUser.getId());
        this.updateById(sysUser);
    }

    @Override
    public UserPermVO getUserByUserIdOrUserName(Long id, String userName) {
        UserPermVO userPermVO = new UserPermVO();
        //1.查询用户信息
        SysUser sysUser = sysUserDao.getSysUserByUserIdOrUserName(userName, id);
        if (sysUser == null) {
            log.warn("getUserByUserIdOrUserName未查询到用户信息:" + userName + "," + id);
            return null;
        }
        //2.通过sysUser获取userPermVO
        getUserPermVoBySysUser(userPermVO, sysUser);
        //3.设置角色信息
        List<SysRoleVo> sysRoleVoList = sysRoleService.queryByUserId(sysUser.getId());
        if (!CollectionUtils.isEmpty(sysRoleVoList)) {
            if (sysRoleVoList != null && sysRoleVoList.size() > 0) {
                List<Long> roleIdList = sysRoleVoList.stream().map(SysRoleVo::getId).collect(Collectors.toList());
                userPermVO.setRoleIds(roleIdList);
                userPermVO.setRoles(getRolesBySysRoleVo(sysRoleVoList));
            }
        }
        return userPermVO;
    }

    @Override
    public SysUserVo getGeneralManagerSysUser() {
        SysEmployeeVo sysEmployeeVo = sysEmployeeService.getGeneralManagerEmployee();
        if (ObjectUtil.isEmpty(sysEmployeeVo)) {
            return null;
        }
        SysUser sysUser = sysUserDao.getSysUserByEmployeeId(sysEmployeeVo.getId());
        if (ObjectUtil.isEmpty(sysUser)) {
            return null;
        }
        return this.objectConversion(sysUser);
    }

    /**
     * 忘记密码
     *
     * @param userName 用户名
     */
    @Override
    public void forgetPassword(String userName) {
        //2.保存用户
        SysUser sysUser = sysUserDao.getSysUserByUserName(userName);
        if (sysUser == null) {
            log.warn("用户信息不存在:" + "," + userName);
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.USER_IS_NOT_EXISTS.getMessage());
        }
        long currentTime = DateUtil.current();
        //生成新的密码
        String newPassword = getRandomPassword();
        sysUser.setPassword(DigestUtil.bcrypt(newPassword));
        //正常
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser("用户重置密码");
        this.updateById(sysUser);
        //3.忘记用户，发送邮件
        resetUserSendEmail(sysUser.getName(), sysUser.getUserName(), sysUser.getMail(), newPassword);
    }

    @Override
    public List<SysUserVo> getAllSysUserVo() {
        SysUserFilterQuery sysUserFilterQuery = new SysUserFilterQuery();
        List<SysUser> sysUserList = sysUserDao.getSysUserByCondition(sysUserFilterQuery);

        return this.objectConversion(sysUserList);
    }


    private void resetUserSendEmail(String name, String userName, String mail, String password) {
        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.FOUR));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(mail);
        mailBeanInfo.setName(name);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));

//        extraMailNotificationDTO.setRequestId(132132132L);
//        extraMailNotificationDTO.setFlowId(132132132L);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        objectMap.put("homepage", "https://esourcing.suntech-power.com.cn");
        objectMap.put("userName", userName);
        objectMap.put("password", password);

        extraMailNotificationInfoDTO.setExtra(objectMap);

        log.info("发送邮件请求报文：{}", JSON.toJSONString(extraMailNotificationInfoDTO));

        workFlowService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 产生随机密码
     *
     * @return {@link String}
     */
    private static String getRandomPassword() {
        int length = 16;//密码长度
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    private List<UserPermVO.Role> getRolesBySysRoleVo(List<SysRoleVo> sysRoleVoList) {

        List<UserPermVO.Role> roleList = new ArrayList<>();
        for (SysRoleVo sysRoleVo : sysRoleVoList) {
            UserPermVO.Role role = new UserPermVO.Role();
            role.setId(sysRoleVo.getId());
            role.setName(sysRoleVo.getName());
            List<SysPermissionVo> sysPermissionVoList = sysPermissionService.getPermissionByRoleId(sysRoleVo.getId());
            if (sysPermissionVoList == null) {
                log.warn("getRolesBySysRoleVo未查询到权限信息:" + sysRoleVoList.toString());
            } else {
                List<Long> permissionIdList = sysPermissionVoList.stream().map(SysPermissionVo::getId).collect(Collectors.toList());
                role.setPermissionIds(permissionIdList);
                role.setPermissions(getPermissionsByRoleId(sysPermissionVoList));
            }
            roleList.add(role);

        }
        return roleList;
    }

    /**
     * sysPermissionVoList 转Permission
     *
     * @param sysPermissionVoList sys许可签证官列表
     */
    private List<UserPermVO.Role.Permission> getPermissionsByRoleId(List<SysPermissionVo> sysPermissionVoList) {
        List<UserPermVO.Role.Permission> permissionList = new ArrayList<>();
        for (SysPermissionVo sysPermissionVo : sysPermissionVoList) {
            UserPermVO.Role.Permission permission = new UserPermVO.Role.Permission();
            permission.setPermission(sysPermissionVo.getPermission());
            if (sysPermissionVo.getIsHide() != null) {
                permission.setHide((Byte.parseByte(sysPermissionVo.getIsHide().toString())));
            } else {
                permission.setHide(Byte.parseByte("0"));
            }
            permission.setIcon(sysPermissionVo.getIcon());
            permission.setLevel(sysPermissionVo.getLevel());
            permission.setParentId(sysPermissionVo.getParentId());
            permission.setRouteUrl(sysPermissionVo.getRouteUrl());
            permission.setSort(sysPermissionVo.getSort());
            permission.setId(sysPermissionVo.getId());
            permission.setName(sysPermissionVo.getName());
            permissionList.add(permission);
        }
        return permissionList;
    }

    /**
     * 通过sysUser获取userPermVO
     *
     * @param userPermVO
     * @param sysUser
     */
    private void getUserPermVoBySysUser(UserPermVO userPermVO, SysUser sysUser) {
        userPermVO.setUserName(sysUser.getUserName());
        userPermVO.setName(sysUser.getName());
        userPermVO.setId(sysUser.getId());
        userPermVO.setMail(sysUser.getMail());
        userPermVO.setEmployeeId(sysUser.getEmployeeId());
        if (sysUser.getSourceType() != null) {
            userPermVO.setSourceType(Byte.parseByte(sysUser.getSourceType()));
        } else {
            userPermVO.setSourceType(Byte.parseByte("1"));//系统添加
        }
        userPermVO.setPassword(sysUser.getPassword());
        if (sysUser.getLockStatus() != null) {
            userPermVO.setLockStatus(Byte.parseByte(sysUser.getLockStatus()));
        } else {
            userPermVO.setLockStatus(Byte.parseByte("0"));
        }
        if (sysUser.getStatus() != null) {
            userPermVO.setStatus(Byte.parseByte(sysUser.getStatus()));
        } else {
            userPermVO.setStatus(Byte.parseByte("0"));
        }
        userPermVO.setPhone(sysUser.getPhone());
    }

}
