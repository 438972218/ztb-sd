package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.pojo.dto.BulletinBoardDTO;
import com.xdcplus.vendor.common.pojo.dto.BulletinBoardFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.BulletinBoard;
import com.xdcplus.vendor.common.pojo.query.BulletinBoardQuery;
import com.xdcplus.vendor.common.pojo.vo.BulletinBoardVO;
import com.xdcplus.vendor.mapper.BulletinBoardMapper;
import com.xdcplus.vendor.service.BulletinBoardService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * 公告牌信息服务实现类
 *
 * @author Rong.Jia
 * @since 2021-08-16
 */
@Slf4j
@Service
public class BulletinBoardServiceImpl extends BaseServiceImpl<BulletinBoard, BulletinBoardVO, BulletinBoard, BulletinBoardMapper> implements BulletinBoardService {

    @Autowired
    private BulletinBoardMapper bulletinBoardMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long syncBulletinBoard(BulletinBoardDTO bulletinBoardDTO) {

        BulletinBoard bulletinBoard = new BulletinBoard();
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreNullValue(Boolean.TRUE);
        BeanUtil.copyProperties(bulletinBoardDTO, bulletinBoard, copyOptions);
        bulletinBoard.setBulletinTime(DateUtil.current());
        bulletinBoard.setCreatedTime(DateUtil.current());
        bulletinBoard.setName(IdUtil.fastSimpleUUID());

        this.save(bulletinBoard);

        return bulletinBoard.getId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteBulletinBoard(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(this.getById(id),
                ResponseEnum.THE_BULLETIN_BOARD_INFO_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED.getMessage());

        return this.removeById(id);
    }

    @Override
    public PageVO<BulletinBoardVO> findBulletinBoard(BulletinBoardFilterDTO pageDTO) {

        PageVO<BulletinBoardVO> pageVO = new PageVO<>();

        if (pageDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(pageDTO);
        }

        BulletinBoardQuery bulletinBoardQuery = new BulletinBoardQuery();
        BeanUtil.copyProperties(pageDTO, bulletinBoardQuery);

        List<BulletinBoard> bulletinBoardList = bulletinBoardMapper.findBulletinBoard(bulletinBoardQuery);
        PageInfo<BulletinBoard> pageInfo = new PageInfo<>(bulletinBoardList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(pageInfo.getList()));

        return pageVO;
    }

    @Override
    public BulletinBoardVO findLatestAnnouncementByRequestId(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<BulletinBoard> bulletinBoardList = bulletinBoardMapper.findBulletinBoardByRequestId(requestId);
        if (CollectionUtil.isNotEmpty(bulletinBoardList)) {
            return this.objectConversion(bulletinBoardList.stream()
                    .max(Comparator.comparing(BulletinBoard::getBulletinTime)).orElse(null));
        }

        return null;
    }
}
