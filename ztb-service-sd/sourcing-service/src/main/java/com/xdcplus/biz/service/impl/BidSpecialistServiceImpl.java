package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.BidSpecialistDTO;
import com.xdcplus.biz.common.pojo.dto.BidSpecialistFilterDTO;
import com.xdcplus.biz.common.pojo.entity.BidVendor;
import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.biz.generator.impl.BidSpecialistBaseServiceImpl;
import com.xdcplus.biz.mapper.BidSpecialistMapper;
import com.xdcplus.biz.common.pojo.entity.BidSpecialist;
import com.xdcplus.biz.common.pojo.vo.BidSpecialistVO;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.BidSpecialistService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysEmployeeVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 专家(BidSpecialist)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-24 16:23:00
 */
@Slf4j
@Service("bidSpecialistService")
public class BidSpecialistServiceImpl extends BidSpecialistBaseServiceImpl<BidSpecialist, BidSpecialistVO, BidSpecialist, BidSpecialistMapper> implements BidSpecialistService {


    @Autowired
    PermService permService;

    @Override
    public PageVO<BidSpecialistVO> queryBidSpecialistWithUser(BidSpecialistFilterDTO bidSpecialistFilterDTO) {

        PageVO<BidSpecialistVO> bidSpecialistVOPageVO = queryBidSpecialist(bidSpecialistFilterDTO);
        List<BidSpecialistVO> bidSpecialistVOS = bidSpecialistVOPageVO.getRecords();
        if(CollectionUtil.isEmpty(bidSpecialistVOS)){
            return bidSpecialistVOPageVO;
        }
        bidSpecialistVOS.forEach(bidSpecialistVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(bidSpecialistVO.getUserId());
            if(ObjectUtil.isNotEmpty(sysUserInfoVO)){
                SysEmployeeVO sysEmployeeVO = permService.getEmployeeVoByUserName(sysUserInfoVO.getUserName());
                bidSpecialistVO.setDepartmentName(sysEmployeeVO.getDepartmentName());
            }
        });
        bidSpecialistVOPageVO.setRecords(bidSpecialistVOS);

        return bidSpecialistVOPageVO;
    }

    @Override
    public BidSpecialistVO saveBidSpecialistReturnVO(BidSpecialistDTO bidSpecialistDTO) {
        List<BidSpecialist> bidSpecialists = bidSpecialistMapper.selectList(new QueryWrapper<BidSpecialist>()
                .lambda().eq(BidSpecialist::getBidSheetId,bidSpecialistDTO.getBidSheetId())
                .eq(BidSpecialist::getUserId,bidSpecialistDTO.getUserId())
                .eq(BidSpecialist::getDeleted,NumberConstant.ZERO));
        if(CollectionUtil.isNotEmpty(bidSpecialists)){
            log.error("saveBidSpecialistReturnVO() bidSpecialists exist");
            throw new ZtbWebException(ResponseEnum.BID_SPECIALIST_EXIST);
        }

        BidSpecialist bidSpecialist = BeanUtil.copyProperties(bidSpecialistDTO, BidSpecialist.class);
        bidSpecialist.setCreatedTime(DateUtil.current());
        bidSpecialist.setDeleted(0);

        boolean result = this.save(bidSpecialist);
        BidSpecialistVO bidSpecialistVO = BeanUtil.copyProperties(bidSpecialist, BidSpecialistVO.class);
        if (result) {
            return bidSpecialistVO;
        } else {
            return null;
        }
    }
}
