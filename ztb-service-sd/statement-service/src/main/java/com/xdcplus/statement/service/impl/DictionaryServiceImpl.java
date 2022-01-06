package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.DictionaryBaseServiceImpl;
import com.xdcplus.statement.mapper.DictionaryMapper;
import com.xdcplus.statement.common.pojo.entity.Dictionary;
import com.xdcplus.statement.common.pojo.vo.DictionaryVO;
import com.xdcplus.statement.service.DictionaryService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 字典管理(Dictionary)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-12 15:20:52
 */
@Slf4j
@Service("dictionaryService")
public class DictionaryServiceImpl extends DictionaryBaseServiceImpl<Dictionary, DictionaryVO, Dictionary, DictionaryMapper> implements DictionaryService {

}
