package com.xdcplus.workflow.mapper;

import com.xdcplus.workflow.common.pojo.bo.DiscussGroupBO;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroup;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 讨论组Mapper 接口
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
public interface DiscussGroupMapper extends IBaseMapper<DiscussGroup> {

    /**
     * 查询讨论组通过表单ID
     *
     * @param requestId 请求id
     * @return {@link List<DiscussGroupBO>} 讨论组
     */
    List<DiscussGroupBO> findDiscussGroupByRequestId(@Param("requestId") Long requestId);

    /**
     * 查询讨论组通过主题和表单ID
     *
     * @param subject   主题
     * @param requestId 表单ID
     * @return {@link DiscussGroup}
     */
    DiscussGroup findDiscussGroupBySubjectAndRequestIdAndCreatedUser(@Param("subject") String subject,
                                                                     @Param("requestId") Long requestId,
                                                                     @Param("createdUser") String createdUser);


}
