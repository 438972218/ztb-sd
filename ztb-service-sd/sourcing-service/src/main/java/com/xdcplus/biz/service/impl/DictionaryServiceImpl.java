package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdcplus.biz.generator.impl.DictionaryBaseServiceImpl;
import com.xdcplus.biz.mapper.DictionaryMapper;
import com.xdcplus.biz.common.pojo.entity.Dictionary;
import com.xdcplus.biz.common.pojo.vo.DictionaryVO;
import com.xdcplus.biz.service.DictionaryService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 字典管理(Dictionary)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-12 15:20:52
 */
@Slf4j
@Service("dictionaryService")
public class DictionaryServiceImpl extends DictionaryBaseServiceImpl<Dictionary, DictionaryVO, Dictionary, DictionaryMapper> implements DictionaryService {


    @Override
    public DictionaryVO findDictionaryVOByClassAndNumerical(String dictionaryClass, int numerical) {
        List<Dictionary> dictionaries = dictionaryMapper.selectList(new QueryWrapper<Dictionary>().lambda().eq(Dictionary::getDictionaryClass,dictionaryClass).eq(Dictionary::getNumerical,numerical));
        if(CollectionUtil.isEmpty(dictionaries)){
            log.error("findDictionaryVOByClassAndNumerical() dictionaries select faild");
            throw new ZtbWebException(ResponseEnum.DATA_DICTIONARY_SELECT_FAILD);
        }else{
            return BeanUtil.copyProperties(dictionaries.get(NumberConstant.ZERO),DictionaryVO.class);
        }
    }
}
