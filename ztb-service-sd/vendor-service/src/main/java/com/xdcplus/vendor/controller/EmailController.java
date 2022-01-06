package com.xdcplus.vendor.controller;


import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 电子邮件控制器
 *
 * @author Fish.Fei
 * @date 2021/10/22
 * @since 2021-07-06 15:16:52
 */
@Api(tags = "邮件")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("email")
public class EmailController extends AbstractController {


    @ApiOperation("向所有的供应商发送注册邮件")
    @GetMapping(value = "sendMail/allVendor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO sendMailWithAllVendor() {

        return ResponseVO.success();
    }


}
