package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xdcplus.biz.common.pojo.dto.PaidAttachmentFilterDTO;
import com.xdcplus.biz.generator.impl.PaidAttachmentBaseServiceImpl;
import com.xdcplus.biz.mapper.PaidAttachmentMapper;
import com.xdcplus.biz.common.pojo.entity.PaidAttachment;
import com.xdcplus.biz.common.pojo.vo.PaidAttachmentVO;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.remote.service.VendorPermService;
import com.xdcplus.biz.service.PaidAttachmentService;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价单附件(PaidAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:01:57
 */
@Slf4j
@Service("paidAttachmentService")
public class PaidAttachmentServiceImpl extends PaidAttachmentBaseServiceImpl<PaidAttachment, PaidAttachmentVO, PaidAttachment, PaidAttachmentMapper> implements PaidAttachmentService {

    @Autowired
    PermService permService;

    @Autowired
    VendorPermService vendorPermService;

    @Override
    public PageVO<PaidAttachmentVO> queryPaidAttachmentWithUser(PaidAttachmentFilterDTO paidAttachmentFilterDTO) {
        PageVO<PaidAttachmentVO> paidAttachmentVOPageVO = queryPaidAttachment(paidAttachmentFilterDTO);
        if(CollectionUtil.isNotEmpty(paidAttachmentVOPageVO.getRecords())){
            paidAttachmentVOPageVO.getRecords().forEach(paidAttachmentVO -> {
                if(paidAttachmentVO.getVendorVisible()==null){
                    SysUserInfoVO sysUserInfoVO = vendorPermService.queryByUserName(paidAttachmentVO.getUploadingUser());
                    if(ObjectUtil.isNotEmpty(sysUserInfoVO)){
                        paidAttachmentVO.setUploadingUserChineseName(sysUserInfoVO.getName());
                    }
                }else{
                    SysUserInfoVO sysUserInfoVO = permService.queryByUserName(paidAttachmentVO.getUploadingUser());
                    if(ObjectUtil.isNotEmpty(sysUserInfoVO)){
                        paidAttachmentVO.setUploadingUserChineseName(sysUserInfoVO.getName());
                    }
                }
            });
        }

        return paidAttachmentVOPageVO;
    }
}
