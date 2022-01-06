package com.xdcplus.vendor.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.xdcplus.vendor.VendorApplicationTest;
import com.xdcplus.vendor.common.pojo.dto.OfferDTO;
import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 出价记录服务测试
 *
 * @author Rong.Jia
 * @date 2021/09/03
 */
class OfferServiceTest extends VendorApplicationTest {

    @Autowired
    private OfferService offerService;

    @Test
    void saveOffer() {

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMoney("20.0");
        offerDTO.setRequestId(1L);
        offerDTO.setOfferGoods("1313231");
        offerDTO.setOfferUser("admin");

        List<OfferDTO> offerDTOList = new ArrayList<>();

        List<OfferDTO> offerDTOList2 = new ArrayList<>();
        OfferDTO offerDTO2 = BeanUtil.copyProperties(offerDTO, OfferDTO.class);
        offerDTOList2.add(BeanUtil.copyProperties(offerDTO2, OfferDTO.class));
        offerDTOList2.add(BeanUtil.copyProperties(offerDTO2, OfferDTO.class));
        offerDTOList2.add(BeanUtil.copyProperties(offerDTO2, OfferDTO.class));
        offerDTOList2.add(BeanUtil.copyProperties(offerDTO2, OfferDTO.class));
        offerDTO2.setChildren(offerDTOList2);


        List<OfferDTO> offerDTOList1 = new ArrayList<>();
        OfferDTO offerDTO1 = BeanUtil.copyProperties(offerDTO, OfferDTO.class);
        offerDTOList1.add(offerDTO2);
        offerDTOList1.add(BeanUtil.copyProperties(offerDTO1, OfferDTO.class));
        offerDTOList1.add(BeanUtil.copyProperties(offerDTO1, OfferDTO.class));
        offerDTOList1.add(offerDTO2);
        offerDTO1.setChildren(offerDTOList1);

        offerDTOList.add(offerDTO1);
        offerDTOList.add(BeanUtil.copyProperties(offerDTO, OfferDTO.class));
        offerDTOList.add(offerDTO1);
        offerDTOList.add(BeanUtil.copyProperties(offerDTO, OfferDTO.class));

        offerDTO.setChildren(offerDTOList);

        offerService.saveOffer(offerDTO);


    }

    @Test
    void findOne() {

        OfferVO offerVO = offerService.findOne(1433687679291760642L);

        System.out.println(offerVO.toString());

    }

    @Test
    void findOfferByOfferGoods() {
    }

    @Test
    void findOffer() {
    }

    @Test
    void getRanking() {

        OfferVO offerVO = offerService.findOne(1433687679291760642L);
        offerService.getRanking(offerVO, true);

        System.out.println(offerVO.toString());


    }

    @Test
    void findCategoryBiddingItems() {

        System.out.println(JSON.toJSONString(offerService.findCategoryBiddingItems(1439122864642007042L)));


    }




}