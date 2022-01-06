package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidSpecialistScoreBaseServiceImpl;
import com.xdcplus.statement.mapper.BidSpecialistScoreMapper;
import com.xdcplus.statement.common.pojo.entity.BidSpecialistScore;
import com.xdcplus.statement.common.pojo.vo.BidSpecialistScoreVO;
import com.xdcplus.statement.service.BidSpecialistScoreService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 招标专家评分(BidSpecialistScore)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-24 16:30:57
 */
@Slf4j
@Service("bidSpecialistScoreService")
public class BidSpecialistScoreServiceImpl extends BidSpecialistScoreBaseServiceImpl<BidSpecialistScore, BidSpecialistScoreVO, BidSpecialistScore, BidSpecialistScoreMapper> implements BidSpecialistScoreService {

}
