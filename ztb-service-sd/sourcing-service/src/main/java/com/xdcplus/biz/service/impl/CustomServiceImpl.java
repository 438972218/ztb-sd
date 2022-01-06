package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.biz.mapper.CustomMapper;
import com.xdcplus.biz.service.CustomService;
import com.xdcplus.ztb.common.remote.domain.sourcing.UserToMarkVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 自定义服务impl
 *
 * @author Fish.Fei
 * @date 2021/09/28
 * @since 2021-08-12 15:51:15
 */
@Slf4j
@Service("customService")
public class CustomServiceImpl implements CustomService {

    @Autowired
    CustomMapper customMapper;

    @Override
    public Long getCustomToUserId(String mark, Long requestId) {
        List<String> marks = CollectionUtil.toList(mark.split("\\."));
        if(marks.size() == NumberConstant.TWO){
            return customMapper.getCustomToUserId(marks.get(0),marks.get(1),requestId);
        }
        return null;
    }

    @Override
    public List<UserToMarkVO> getUserToMarkVO() {
        List<UserToMarkVO> userToMarkVOS=CollectionUtil.newArrayList();
        userToMarkVOS.add(UserToMarkVO.builder()
                        .userToMark("scm_vendor_kpi.responsible_person_id")
                        .description("供应商绩效的负责人")
                        .build());
        return userToMarkVOS;
    }
}
