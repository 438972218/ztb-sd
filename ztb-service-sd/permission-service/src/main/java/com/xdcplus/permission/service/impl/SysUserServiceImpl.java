package com.xdcplus.permission.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageInfo;
import com.xdcplus.permission.common.pojo.dto.sysuser.*;
import com.xdcplus.permission.common.pojo.entity.SysEmployee;
import com.xdcplus.permission.common.pojo.vo.sysuser.GetAllUserAndDepartAndPostionVO;
import com.xdcplus.permission.common.pojo.vo.sysuser.GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO;
import com.xdcplus.permission.constants.PermConstant;
import com.xdcplus.permission.remote.EmailRemote;
import com.xdcplus.permission.remote.pojo.dto.MailBean;
import com.xdcplus.permission.remote.pojo.dto.RegisterNotificationDTO;
import com.xdcplus.permission.remote.service.WorkFlowService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.permission.common.enums.IsDeletedEnum;
import com.xdcplus.permission.dao.SysUserDao;
import com.xdcplus.permission.common.pojo.entity.SysUser;
import com.xdcplus.permission.common.pojo.entity.SysUserRole;
import com.xdcplus.permission.common.pojo.query.sysuser.SysUserFilterQuery;
import com.xdcplus.permission.common.pojo.vo.sysPermission.SysPermissionVo;
import com.xdcplus.permission.common.pojo.vo.sysdepartment.SysDepartmentVo;
import com.xdcplus.permission.common.pojo.vo.sysemployee.SysEmployeeVo;
import com.xdcplus.permission.common.pojo.vo.sysrole.SysRoleVo;
import com.xdcplus.permission.common.pojo.vo.sysuser.SysUserVo;
import com.xdcplus.permission.common.pojo.vo.sysuser.UserPermVO;
import com.xdcplus.permission.service.*;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.MailBeanInfo;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
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
 * ???????????????(SysUser)??????????????????
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
    private EmailRemote emailRemote;
    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public PageVO<SysUserVo> getSysUserServicePageByCondition(SysUserFilterDto sysUserFilterDto) {
        PageVO<SysUserVo> pageVo = new PageVO<>();
        //1.??????????????????
        SysUserFilterQuery sysUserFilterQuery = new SysUserFilterQuery();
        //2.????????????
        if (sysUserFilterDto.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(sysUserFilterDto.getCurrentPage(), sysUserFilterDto.getPageSize(),
                    sysUserFilterDto.getOrderType(), sysUserFilterDto.getOrderField());
        }
        //3.???dto???????????????query??????
        org.springframework.beans.BeanUtils.copyProperties(sysUserFilterDto, sysUserFilterQuery);
        //4.??????
        List<SysUser> sysUserList = sysUserDao.getSysUserByCondition(sysUserFilterQuery);
        PageInfo pageInfo = new PageInfo<>(sysUserList);
        PropertyUtils.copyProperties(pageInfo, pageVo);
        pageVo.setRecords(this.objectConversion(sysUserList));
        if(pageVo.getRecords()!=null){
            for (SysUserVo sysUserVo:pageVo.getRecords()) {
                //4.1 ???????????????????????????????????????
                List<SysUserRole> SysUserRoleList=sysUserRoleService.getByUserId(sysUserVo.getId());
                if(SysUserRoleList!=null && SysUserRoleList.size()>0){
                    List<Long>roleIdList=SysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    sysUserVo.setSysRoleVoList(roleIdList);
                }
                //4.2 ????????????id??????????????????
                if(sysUserVo.getEmployeeId()!=null){
                    SysEmployeeVo sysEmployeeVo = sysEmployeeService.queryById(sysUserVo.getEmployeeId());
                    if(ObjectUtil.isNotEmpty(sysEmployeeVo)){
                        sysUserVo.setEmployeeName(sysEmployeeVo.getRealName());
                    }
                }
                //4.3 ??????????????????????????????
                if(sysUserVo.getCreatedUser()!=null){
                    SysUser sysUser = sysUserDao.getSysUserByUserName(sysUserVo.getCreatedUser());
                    if(ObjectUtil.isNotEmpty(sysUser)){
                        sysUserVo.setCreatedUserName(sysUser.getName());
                    }
                }
            }
        }

        return pageVo;
    }

    @Override
    public SysUserVo getSysUserManagerByDepartmentId(Long departmentId) {
        SysDepartmentVo sysDepartmentVo=  sysDepartmentService.queryById(departmentId);
        if(ObjectUtil.isEmpty(sysDepartmentVo)){
            log.error("departmentId?????????"+departmentId);
            throw new ZtbWebException(ResponseEnum.DEPARTMENT_ID_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DEPARTMENT_ID_IS_NOT_EXISTS.getMessage());
        }
        if(sysDepartmentVo.getManager()==null){
            log.error("????????????????????????"+departmentId);
            throw new ZtbWebException(ResponseEnum.DEPARTMENT_MANAGER_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DEPARTMENT_MANAGER_IS_NOT_EXISTS.getMessage());
        }
        SysUser sysUser=sysUserDao.getSysUserByEmployeeId(sysDepartmentVo.getManager());
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
        SysUserFilterQuery sysUserFilterQuery =new SysUserFilterQuery();
        List<SysUser> sysUsers = sysUserDao.getSysUserByCondition(sysUserFilterQuery);
        if(CollectionUtil.isEmpty(sysUsers)){
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
        //1.??????????????????????????????
        if(getSysUserByUserIdOrUserNameDto==null){
            throw new ZtbWebException(ResponseEnum.REQUEST_PARAMETER_IS_NULL);
        }
        if(StringUtils.isBlank(getSysUserByUserIdOrUserNameDto.getUserName()) && getSysUserByUserIdOrUserNameDto.getId()==null ){
            throw new ZtbWebException(ResponseEnum.USER_NAME_AND_USER_ID_IS_ALL_NULL.getCode(),
                    ResponseEnum.USER_NAME_AND_USER_ID_IS_ALL_NULL.getMessage());
        }
        //2.???????????????????????????id??????????????????
        SysUser sysUser= sysUserDao.getSysUserByUserIdOrUserName(getSysUserByUserIdOrUserNameDto.getUserName(),getSysUserByUserIdOrUserNameDto.getId());
        if(sysUser==null){
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        //3.????????????????????????????????????id
        if(sysUser.getEmployeeId()==null){
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EMPLOYEE_ID.getCode(),
                    ResponseEnum.USER_IS_NOT_EMPLOYEE_ID.getMessage());
        }
        //4.????????????id????????????????????????
        SysEmployeeVo sysEmployeeVo= sysEmployeeService.queryById(sysUser.getEmployeeId());
        if(sysEmployeeVo==null){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getMessage());
        }
        //6.?????????????????????????????????????????????????????????????????????
        if(sysEmployeeVo.getManagerId()==null ){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getMessage());
        }
        SysEmployeeVo managerIdVo= sysEmployeeService.queryById(sysEmployeeVo.getManagerId());
        if(managerIdVo==null){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_MANAGER_IS_NOT_EXIST.getMessage());
        }
        SysUser sysMangerUser=sysUserDao.getSysUserByEmployeeId(managerIdVo.getId());
        return this.objectConversion(sysMangerUser);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insert(SysUserDto sysUserDto,String loginUser) {
        //1.???????????????????????????
        insertValidate(sysUserDto);
        //2.????????????
        SysUser sysUser = new SysUser();
        org.springframework.beans.BeanUtils.copyProperties(sysUserDto, sysUser);
        long currentTime=DateUtil.current();
        sysUser.setPassword(DigestUtil.bcrypt(sysUserDto.getPassword()));
        sysUser.setStatus("0");//??????
        sysUser.setLockStatus("0");
        sysUser.setUpdatedTime(currentTime);
        sysUser.setCreatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setCreatedUser(loginUser);
        sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
        this.save(sysUser);
        //3.??????????????????????????????
        saveUserRole(sysUserDto, sysUser,loginUser);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SysUserVo registerUser(RegisterUserDto registerUserDto) {
        //1.???????????????????????????
        SysUser sysUserNameExists=sysUserDao.getSysUserByUserNameAndNoId(registerUserDto.getUserName(),null);
        if(sysUserNameExists!=null){
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //2.????????????????????????
        SysUser sysUserEmailExists=sysUserDao.getSysUserByEmail(registerUserDto.getMail());
        if(sysUserEmailExists!=null && (!registerUserDto.getUserName().equals(sysUserEmailExists.getUserName()))){
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_EMAIL_IS_EXIST.getMessage());
        }
        //2.????????????
        SysUser sysUser = new SysUser();
        org.springframework.beans.BeanUtils.copyProperties(registerUserDto, sysUser);
        long currentTime=DateUtil.current();
        sysUser.setPassword(DigestUtil.bcrypt(registerUserDto.getPassword()));
        //??????
        sysUser.setStatus("0");
        sysUser.setLockStatus("0");
        sysUser.setCreatedTime(currentTime);
        sysUser.setCreatedUser("????????????");
        sysUser.setDeleted(IsDeletedEnum.NO_DELETED.getCode());
        this.save(sysUser);
        //3.???????????????????????????
        registerUserSendEmail(sysUser.getUserName(),sysUser.getMail(),registerUserDto.getPassword());
        return this.objectConversion(sysUser);
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     *
     * @return {@link SysUserVo}
     */
    @Override
    public List<GetAllUserAndDepartAndPostionVO> findAllUserAndDepartAndPostion() {
        return sysUserDao.findAllUserAndDepartAndPostion();
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param userName ?????????
     * @return {@link GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO}
     */
    @Override
    public GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO getDepartmentManagerEmployeeVoAndSysUserVoByUserName(String userName) {
        GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO=new GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO();
        //1.?????????????????????????????????
       SysUser sysUser= sysUserDao.getSysUserByUserName(userName);
       if(sysUser!=null && sysUser.getEmployeeId()!=null){
           //2.?????????????????????id??????????????????????????????
           SysEmployeeVo sysEmployee= sysEmployeeService.queryById(sysUser.getEmployeeId());
           if(sysEmployee!=null && sysEmployee.getDepartmentId()!=null){
               //3.????????????????????????????????????????????????
               SysDepartmentVo sysDepartmentVo= sysDepartmentService.queryById(sysEmployee.getDepartmentId());
               if(sysDepartmentVo!=null && sysDepartmentVo.getManager()!=null){
                   //4.??????????????????????????????
                 SysEmployeeVo sysEmployeeDepart=sysEmployeeService.queryById(sysDepartmentVo.getManager());
                 if(sysEmployeeDepart!=null){
                     //5.????????????????????????????????????
                     getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO.setSysEmployeeVo(sysEmployeeDepart);
                    //6.????????????????????????????????????
                     SysUser sysUserDepart=  sysUserDao.getSysUserByEmployeeId(sysDepartmentVo.getManager());
                     if(sysUserDepart!=null){
                         getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO.setSysUserVo(this.objectConversion(sysUserDepart));
                     }
                     //????????????????????????????????????????????????
                     if(sysEmployeeDepart.getDepartmentId()!=null){
                       SysDepartmentVo SysDepartmentMangerVo =sysDepartmentService.queryById(sysEmployeeDepart.getDepartmentId());
                       if(SysDepartmentMangerVo!=null){
                           sysEmployeeDepart.setDepartmentName(SysDepartmentMangerVo.getShortName());
                       }
                     }
                 }
               }
           }
       }
        return getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO;
    }


    /**
     * ???????????????????????????
     * @param userName ?????????
     * @param mail ??????
     */
    private void registerUserSendEmail(String userName,String mail,String password) {
        RegisterNotificationDTO registerNotificationDTO=new RegisterNotificationDTO();
        MailBean mailBean =new MailBean();
        mailBean.setMail(mail);
        List<MailBean> mailBeanList=new ArrayList<>();
        mailBeanList.add(mailBean);
        registerNotificationDTO.setTo(mailBeanList);
        registerNotificationDTO.setMailSubject("??????????????????");
        registerNotificationDTO.setUsername(userName);
        registerNotificationDTO.setAccount(userName);
        registerNotificationDTO.setPassword(password);
        log.info("???????????????????????????{}", JSON.toJSONString(registerNotificationDTO));
        ResponseVO<String> result= emailRemote.sendRegisterNotification(registerNotificationDTO);
        if(result!=null){
            log.info("???????????????????????????{}",JSON.toJSONString(result));
        }
        //1
        if(result==null || StringUtils.isBlank(result.getData())){
            log.info("????????????????????????????????????{}??????????????????{}???",JSON.toJSONString(registerNotificationDTO),JSON.toJSONString(result));
            throw new ZtbWebException(-1,"??????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param sysUserDto ????????????dto
     */
    private void insertValidate(SysUserDto sysUserDto) {
        //1.???????????????????????????
        SysUser sysUserNameExists=sysUserDao.getSysUserByUserNameAndNoId(sysUserDto.getUserName(),null);
        if(sysUserNameExists!=null){
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //2.???????????????????????????
        SysEmployeeVo sysEmployeeVo= sysEmployeeService.queryById(sysUserDto.getEmployeeId());
        if(sysEmployeeVo==null){
            throw new ZtbWebException(ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getMessage());
        }
        //3.??????????????????????????????????????????
        List<SysUser> sysUserByEmployeeIdList=sysUserDao.getSysUserByEmployeeAndNoId(sysUserDto.getEmployeeId(),null);
        if(sysUserByEmployeeIdList!=null && sysUserByEmployeeIdList.size()>0){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_Bind.getCode(),
                    ResponseEnum.EMPLOYEE_IS_Bind.getMessage());
        }
        //4.????????????
        if(sysUserDto.getSysRoleVoList()==null || sysUserDto.getSysRoleVoList().size()==0){
            throw new ZtbWebException(ResponseEnum.USER_ROLE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.USER_ROLE_IS_NOT_EXISTS.getMessage());
        }
    }

    /**
     * ??????????????????????????????
     * @param sysUserDto
     * @param sysUser
     */
    private void saveUserRole(SysUserDto sysUserDto, SysUser sysUser,String loginUser) {
        for(Long roleId: sysUserDto.getSysRoleVoList()){
            if(roleId!=null){
                //??????role_id????????????
                SysRoleVo sysRoleVo=  sysRoleService.queryById(roleId);
                if(sysRoleVo==null){
                    throw new ZtbWebException(ResponseEnum.USER_ROLE_BIND_ERROR.getCode(),
                            ResponseEnum.USER_ROLE_BIND_ERROR.getMessage());
                }
            }
            long currentTime=DateUtil.current();
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreatedTime(currentTime);
            sysUserRole.setCreatedUser(loginUser);
            sysUserRoleService.insert(sysUserRole);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(SysUserDto sysUserDto,String loginUser) {
        //1.????????????
        SysUser sysUser = updateByIdValidate(sysUserDto);
        //2.??????????????????
        //2.1?????????????????????????????????
        sysUserRoleService.deleteByUserId(sysUser.getId());
        //2.2??????????????????????????????
        saveUserRole(sysUserDto,sysUser,loginUser);
        org.springframework.beans.BeanUtils.copyProperties(sysUserDto, sysUser);
        long currentTime=DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setPassword(null);//????????????????????????????????????;???????????????????????????????????????????????????????????????????????????????????????????????????,??????????????? yanjinyin
        //3.????????????
        this.updateById(sysUser);
    }

    @Override
    public void updatePasswordById(UpdatePasswordByIdDto updatePasswordByIdDto,String loginUser) {
        //1.??????id????????????
        SysUser sysUser = this.getById(updatePasswordByIdDto.getId());
        if (sysUser == null) {
            log.error("??????id?????????:"+updatePasswordByIdDto.toString());
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        if(!DigestUtil.bcryptCheck(updatePasswordByIdDto.getOldPassword(),sysUser.getPassword())){
            log.error("???????????????????????????:"+updatePasswordByIdDto.toString());
            throw new ZtbWebException(ResponseEnum.USER_OLD_PASSWORD_IS_ERROR.getCode(),
                    ResponseEnum.USER_OLD_PASSWORD_IS_ERROR.getMessage());
        }

        sysUser.setPassword(DigestUtil.bcrypt(updatePasswordByIdDto.getNewPassword()));
        long currentTime=DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        //????????????
        this.updateById(sysUser);
    }

    /**
     * ????????????
     *
     * @param userName ?????????
     */
    @Override
    public void forgetPassword(String userName) {
        //2.????????????
        SysUser sysUser = sysUserDao.getSysUserByUserName(userName);
        if(sysUser==null){
            log.warn("?????????????????????:"+","+userName);
            throw new ZtbWebException(ResponseEnum.USER_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.USER_IS_NOT_EXISTS.getMessage());
        }
        long currentTime=DateUtil.current();
        //??????????????????
        String  newPassword=getRandomPassword();
        sysUser.setPassword(DigestUtil.bcrypt(newPassword));
        //??????
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser("??????????????????");
        this.updateById(sysUser);
        //3.???????????????????????????
        resetUserSendEmail(sysUser.getName(),sysUser.getUserName(),sysUser.getMail(),newPassword);
    }

    @Override
    public void resetPasswordById(Long id,String loginUser) {
        //1.??????id????????????
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            log.error("??????id?????????:"+id);
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        sysUser.setPassword(DigestUtil.bcrypt(PermConstant.USER_PASSWORD));
        long currentTime=DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        //????????????
        this.updateById(sysUser);
    }

    /**
     * ???????????????????????????
     * @param userName ?????????
     * @param mail ??????
     */
    private void resetUserSendEmail(String name,String userName,String mail,String password) {
        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO=new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.FOUR));

        MailBeanInfo mailBeanInfo=new MailBeanInfo();
        mailBeanInfo.setMail(mail);
        mailBeanInfo.setName(name);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));

//        extraMailNotificationDTO.setRequestId(132132132L);
//        extraMailNotificationDTO.setFlowId(132132132L);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        objectMap.put("homepage", "https://epurchase.suntech-power.com.cn");
        objectMap.put("userName", userName);
        objectMap.put("password", password);

        extraMailNotificationInfoDTO.setExtra(objectMap);

        log.info("???????????????????????????{}", JSON.toJSONString(extraMailNotificationInfoDTO));

        workFlowService.emailNotification(extraMailNotificationInfoDTO);

//        RegisterNotificationDTO registerNotificationDTO=new RegisterNotificationDTO();
//        MailBean mailBean =new MailBean();
//        mailBean.setMail(mail);
//        List<MailBean> mailBeanList=new ArrayList<>();
//        mailBeanList.add(mailBean);
//        registerNotificationDTO.setTo(mailBeanList);
//        registerNotificationDTO.setMailSubject("??????????????????");
//        registerNotificationDTO.setUsername(name);
//        registerNotificationDTO.setAccount(userName);
//        registerNotificationDTO.setPassword(password);
//        log.info("???????????????????????????{}", JSON.toJSONString(registerNotificationDTO));
//        ResponseVO<String> result= emailRemote.sendRegisterNotification(registerNotificationDTO);
//        if(result!=null){
//            log.info("???????????????????????????{}",JSON.toJSONString(result));
//        }
//        //1
//        if(result==null || StringUtils.isBlank(result.getData())){
//            log.info("????????????????????????????????????{}??????????????????{}???",JSON.toJSONString(registerNotificationDTO),JSON.toJSONString(result));
//            throw new ZtbWebException(-1,"??????????????????");
//        }
    }


    /**
     * ??????????????????
     *
     * @return {@link String}
     */
    private static String getRandomPassword(){
        int length=16;//????????????
        //????????????????????????A-Z???a-z???0-9??????62??????
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //???Random???????????????
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //???????????????????????????
        for(int i=0; i<length; ++i){
            //??????0-61?????????
            int number=random.nextInt(62);
            //????????????????????????length????????????sb???
            sb.append(str.charAt(number));
        }
        //????????????????????????????????????
        return sb.toString();
    }

    /**
     * ??????id???????????????????????????
     *
     * @param sysUserDto ????????????dto
     * @return {@link SysUser}
     */
    private SysUser updateByIdValidate(SysUserDto sysUserDto) {
        //1.??????id????????????
        SysUser sysUser = this.getById(sysUserDto.getId());
        if (sysUser == null) {
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        //2.???????????????????????????
        SysUser sysUserNameExists=sysUserDao.getSysUserByUserNameAndNoId(sysUserDto.getUserName(), sysUserDto.getId());
        if(sysUserNameExists!=null){
            throw new ZtbWebException(ResponseEnum.SYS_SYSUSER_IS_EXIST.getCode(),
                    ResponseEnum.SYS_SYSUSER_IS_EXIST.getMessage());
        }
        //3.???????????????????????????
        SysEmployeeVo sysEmployeeVo= sysEmployeeService.queryById(sysUserDto.getEmployeeId());
        if(sysEmployeeVo==null){
            throw new ZtbWebException(ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.SYS_EMPLOYEE_IS_NOT_EXISTS.getMessage());
        }
        //5.??????????????????????????????????????????
        List<SysUser> sysUserByEmployeeIdList=sysUserDao.getSysUserByEmployeeAndNoId(sysUser.getEmployeeId(), sysUserDto.getId());
        if(sysUserByEmployeeIdList!=null && sysUserByEmployeeIdList.size()>0){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_Bind.getCode(),
                    ResponseEnum.EMPLOYEE_IS_Bind.getMessage());
        }
        return sysUser;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long id,String loginUser) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new ZtbWebException(ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getCode(),
                    ResponseEnum.DATA_RECORD_IS_NOT_EXISTS.getMessage());
        }
        long currentTime= DateUtil.current();
        sysUser.setUpdatedTime(currentTime);
        sysUser.setUpdatedUser(loginUser);
        sysUser.setDeleted(IsDeletedEnum.DELETED.getCode());//??????
        sysUserRoleService.deleteByUserId(sysUser.getId());
        this.updateById(sysUser);
    }

    @Override
    public UserPermVO getUserByUserIdOrUserName(Long id, String userName) {
        UserPermVO userPermVO=new UserPermVO();
        //1.??????????????????
        SysUser sysUser= sysUserDao.getSysUserByUserIdOrUserName(userName,id);
        if(sysUser==null){
            log.warn("getUserByUserIdOrUserName????????????????????????:"+userName+","+id);
            return null;
        }
        //2.??????sysUser??????userPermVO
        getUserPermVoBySysUser(userPermVO, sysUser);
        //3.??????????????????
        List<SysRoleVo> sysRoleVoList= sysRoleService.queryByUserId(sysUser.getId());
        if (!CollectionUtils.isEmpty(sysRoleVoList)) {
            if(sysRoleVoList!=null && sysRoleVoList.size()>0){
                List<Long>roleIdList=sysRoleVoList.stream().map(SysRoleVo::getId).collect(Collectors.toList());
                userPermVO.setRoleIds(roleIdList);
                userPermVO.setRoles(getRolesBySysRoleVo(sysRoleVoList));
            }
        }
        //??????????????????
        if(sysUser.getEmployeeId()!=null){
            SysEmployeeVo sysEmployeeVo = sysEmployeeService.getEmployeeVoWithDepartmentById(sysUser.getEmployeeId());
            if(ObjectUtil.isNotEmpty(sysEmployeeVo)){
                userPermVO.setDepartmentName(sysEmployeeVo.getDepartmentName());
            }
        }

        return userPermVO;
    }

    @Override
    public SysUserVo getGeneralManagerSysUser() {
        SysEmployeeVo sysEmployeeVo= sysEmployeeService.getGeneralManagerEmployee();
        if(ObjectUtil.isEmpty(sysEmployeeVo)){
            return null;
        }
        SysUser sysUser=  sysUserDao.getSysUserByEmployeeId(sysEmployeeVo.getId());
        if(ObjectUtil.isEmpty(sysUser)){
            return null;
        }
        return this.objectConversion(sysUser);
    }

    @Override
    public List<String> getSubordinateUserNames(String userName) {
        SysEmployeeVo sysEmployeeVo = sysEmployeeService.getEmployeeVoByUserName(userName);
        SysUserVo sysUserVo = queryByUserName(userName);
        if(ObjectUtil.isEmpty(sysEmployeeVo)){
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getCode(),
                    ResponseEnum.EMPLOYEE_IS_NOT_EXIST.getMessage());
        }
        if(ObjectUtil.isEmpty(sysUserVo)){
            throw new ZtbWebException(ResponseEnum.ACCOUNT_DOES_NOT_EXIST.getCode(),
                    ResponseEnum.ACCOUNT_DOES_NOT_EXIST.getMessage());
        }
        List<Long> employeeIds= CollectionUtil.newArrayList(sysUserVo.getId());
        combineEmployeeId(employeeIds,sysEmployeeVo.getId());
        if(CollectionUtil.isEmpty(employeeIds)){
            return null;
        }
        List<String> sysUserIds = sysUserDao.getSysUserByEmployeeNames(employeeIds);

        return sysUserIds;
    }

    @Override
    public List<SysUserVo> getSysUserByRoleId(Long roleId) {
        List<SysUser> sysUsers = sysUserDao.getSysUserByRoleId(roleId);

        return this.objectConversion(sysUsers);
    }

    private void combineEmployeeId(List<Long> employeeIds,Long employeeId){
        List<SysEmployeeVo> sysEmployeeVos = sysEmployeeService.getSysEmployeeListByManagerId(employeeId);
        employeeIds.add(employeeId);
        if(CollectionUtil.isEmpty(sysEmployeeVos)){
            return;
        }
        sysEmployeeVos.forEach(sysEmployeeVo -> {
            combineEmployeeId(employeeIds,sysEmployeeVo.getId());
        });
    }

    private List<UserPermVO.Role> getRolesBySysRoleVo( List<SysRoleVo> sysRoleVoList){

        List<UserPermVO.Role> roleList=new ArrayList<>();
        for(SysRoleVo sysRoleVo:sysRoleVoList){
            UserPermVO.Role role=new UserPermVO.Role();
            role.setId(sysRoleVo.getId());
            role.setName(sysRoleVo.getName());
            List<SysPermissionVo> sysPermissionVoList= sysPermissionService.getPermissionByRoleId(sysRoleVo.getId());
            if(sysPermissionVoList==null){
                log.warn("getRolesBySysRoleVo????????????????????????:"+sysRoleVoList.toString());
            }else{
                List<Long>permissionIdList=sysPermissionVoList.stream().map(SysPermissionVo::getId).collect(Collectors.toList());
                role.setPermissionIds(permissionIdList);
                role.setPermissions(getPermissionsByRoleId(sysPermissionVoList));
            }
            roleList.add(role);

        }
        return roleList;
    }

    /**
     * sysPermissionVoList ???Permission
     *
     * @param sysPermissionVoList sys?????????????????????
     */
    private List<UserPermVO.Role.Permission> getPermissionsByRoleId(List<SysPermissionVo>  sysPermissionVoList){
        List<UserPermVO.Role.Permission>  permissionList=new ArrayList<>();
        for(SysPermissionVo sysPermissionVo:sysPermissionVoList){
            UserPermVO.Role.Permission permission=new UserPermVO.Role.Permission();
            permission.setPermission(sysPermissionVo.getPermission());
            if(sysPermissionVo.getIsHide()!=null){
                permission.setHide((Byte.parseByte(sysPermissionVo.getIsHide().toString())));
            }else{
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
     * ??????sysUser??????userPermVO
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
        if(sysUser.getSourceType()!=null){
            userPermVO.setSourceType(Byte.parseByte(sysUser.getSourceType()));
        }else{
            userPermVO.setSourceType(Byte.parseByte("1"));//????????????
        }
        userPermVO.setPassword(sysUser.getPassword());
        if(sysUser.getLockStatus()!=null){
            userPermVO.setLockStatus(Byte.parseByte(sysUser.getLockStatus()));
        }else{
            userPermVO.setLockStatus(Byte.parseByte("0"));
        }
        if(sysUser.getStatus()!=null){
            userPermVO.setStatus(Byte.parseByte(sysUser.getStatus()));
        }else{
            userPermVO.setStatus(Byte.parseByte("0"));
        }
        userPermVO.setPhone(sysUser.getPhone());
    }

}
