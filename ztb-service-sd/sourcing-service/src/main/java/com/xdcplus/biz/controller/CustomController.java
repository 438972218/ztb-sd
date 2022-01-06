package com.xdcplus.biz.controller;


import com.xdcplus.biz.common.pojo.dto.VendorUserDTO;
import com.xdcplus.biz.common.pojo.dto.VendorUserFilterDTO;
import com.xdcplus.biz.common.pojo.vo.VendorUserVO;
import com.xdcplus.biz.service.CustomService;
import com.xdcplus.biz.service.VendorUserService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.remote.domain.sourcing.UserToMarkVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 供应商用户(VendorUser)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:28
 */
@Api(tags = "业务通用接口(Custom)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("custom")
public class CustomController extends AbstractController {

    @Autowired
    private CustomService customService;

    @ApiOperation("根据标识和requestId查询出toUserId")
    @GetMapping(value = "/getCustomToUserId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<Long> getCustomToUserId(String mark,Long requestId) {

        log.info("getCustomToUserId {}", requestId);

        Validation.buildDefaultValidatorFactory().getValidator().validate(requestId);

        return ResponseVO.success(customService.getCustomToUserId(mark,requestId));
    }

    @ApiOperation("查询出用户去向标识列表")
    @GetMapping(value = "/getUserToMarkVO", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<List<UserToMarkVO>> getUserToMarkVO() {

        return ResponseVO.success(customService.getUserToMarkVO());
    }

}
