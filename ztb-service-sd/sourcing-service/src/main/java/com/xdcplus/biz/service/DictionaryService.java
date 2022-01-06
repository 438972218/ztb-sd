package com.xdcplus.biz.service;

import com.xdcplus.biz.generator.DictionaryBaseService;
import com.xdcplus.biz.common.pojo.entity.Dictionary;
import com.xdcplus.biz.common.pojo.vo.DictionaryVO;


/**
 * 字典管理(Dictionary)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-12 15:20:52
 */
public interface DictionaryService extends DictionaryBaseService<Dictionary, DictionaryVO, Dictionary> {

    /**
     * 找到字典voby类和数值
     *
     * @param dictionaryClass 字典类
     * @param numerical       数值
     * @return {@link DictionaryVO}
     */
    DictionaryVO findDictionaryVOByClassAndNumerical(String dictionaryClass,int numerical);

}
