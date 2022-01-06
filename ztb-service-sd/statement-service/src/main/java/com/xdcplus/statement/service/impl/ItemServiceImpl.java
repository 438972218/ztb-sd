package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.ItemBaseServiceImpl;
import com.xdcplus.statement.mapper.ItemMapper;
import com.xdcplus.statement.common.pojo.entity.Item;
import com.xdcplus.statement.common.pojo.vo.ItemVO;
import com.xdcplus.statement.service.ItemService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 品类管理(Item)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-18 13:47:07
 */
@Slf4j
@Service("itemService")
public class ItemServiceImpl extends ItemBaseServiceImpl<Item, ItemVO, Item, ItemMapper> implements ItemService {


}
