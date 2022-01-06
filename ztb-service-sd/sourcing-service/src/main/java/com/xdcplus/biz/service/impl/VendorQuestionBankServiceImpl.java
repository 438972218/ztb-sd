package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.biz.common.pojo.dto.VendorQuestionBankFilterDTO;
import com.xdcplus.biz.generator.impl.VendorQuestionBankBaseServiceImpl;
import com.xdcplus.biz.mapper.VendorQuestionBankMapper;
import com.xdcplus.biz.common.pojo.entity.VendorQuestionBank;
import com.xdcplus.biz.common.pojo.vo.VendorQuestionBankVO;
import com.xdcplus.biz.service.VendorQuestionBankService;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板题库表(VendorQuestionBank)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:29
 */
@Slf4j
@Service("vendorQuestionBankService")
public class VendorQuestionBankServiceImpl extends VendorQuestionBankBaseServiceImpl<VendorQuestionBank, VendorQuestionBankVO, VendorQuestionBank, VendorQuestionBankMapper> implements VendorQuestionBankService {


    @Override
    public List<VendorQuestionBankVO> queryWithChild(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO) {
        List<VendorQuestionBankVO> vendorQuestionBankVOS = queryVendorQuestionBankVOList(vendorQuestionBankFilterDTO);
        if(CollectionUtil.isEmpty(vendorQuestionBankVOS)){
            return null;
        }
        List<VendorQuestionBankVO> vendorQuestionBankVOS1 = vendorQuestionBankVOS.stream()
                .filter(vendorKpiDetailVO -> vendorKpiDetailVO.getParentId().equals(NumberConstant.ZERO.longValue())).collect(Collectors.toList());

        for (VendorQuestionBankVO vendorQuestionBankVO : vendorQuestionBankVOS1) {
            List<VendorQuestionBankVO> childVendorQuestionBankVOS = vendorQuestionBankVOS.stream()
                    .filter(vendorKpiDetailVO1 ->
                            vendorKpiDetailVO1.getParentId().equals(vendorQuestionBankVO.getId())).collect(Collectors.toList());
            vendorQuestionBankVO.setChildVendorQuestionBankVOS(childVendorQuestionBankVOS);
        }


        return vendorQuestionBankVOS1;

    }
}
