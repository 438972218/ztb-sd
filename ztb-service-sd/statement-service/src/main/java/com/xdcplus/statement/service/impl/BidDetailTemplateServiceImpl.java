package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidDetailTemplateBaseServiceImpl;
import com.xdcplus.statement.mapper.BidDetailTemplateMapper;
import com.xdcplus.statement.common.pojo.entity.BidDetailTemplate;
import com.xdcplus.statement.common.pojo.vo.BidDetailTemplateVO;
import com.xdcplus.statement.service.BidDetailTemplateService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 内容模板(BidDetailTemplate)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:18
 */
@Slf4j
@Service("bidDetailTemplateService")
public class BidDetailTemplateServiceImpl extends BidDetailTemplateBaseServiceImpl<BidDetailTemplate, BidDetailTemplateVO, BidDetailTemplate, BidDetailTemplateMapper> implements BidDetailTemplateService {


}
