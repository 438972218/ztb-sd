package com.xdcplus.workflow.mapper;

import com.xdcplus.workflow.common.pojo.entity.UserTo;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *  用户去向信息 Mapper 接口
 *
 * @author Rong.Jia
 * @date  2021-07-01
 */
public interface UserToMapper extends IBaseMapper<UserTo> {

    /**
     * 根据标识查询用户去向信息
     * @param mark 标识
     * @return 用户去向信息
     */
    UserTo findUserToByMark(@Param("mark") Integer mark);


}
