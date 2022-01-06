package com.xdcplus.watermark;

import com.xdcplus.ztb.common.exceptions.WatermarkException;
import com.xdcplus.ztb.common.watermark.WatermarkUtils;
import com.xdcplus.ztb.common.watermark.domain.WatermarkParam;
import org.junit.Test;

import java.awt.*;
import java.io.File;

public class WatermarkProcessorTest {

    @Test
    public void image() throws WatermarkException {

        File file = new File("F:\\4k性感美女图片_3 - 副本.jpg");
        File imageFile = new File("F:\\1 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(30F)
//                .imageFile(imageFile)
//                .xMove(100)
//                .yMove(100)
                .alpha(1F)
                .bespread(Boolean.TRUE)
                .color(Color.red)
                .build();


        WatermarkUtils.addWatermark(param);

    }

    @Test
    public void pdf() throws Exception {

        File file = new File("C:\\tmp\\conventer\\b5ab8529722a9e5795843adc0b2b34034bd5562cfa3e6a2ead9ab803897dedcb\\74b1f1eb8d2b436697630f6886ba19d2.pdf");
        File imageFile = new File("F:\\3 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
//                .degree(40.0F)
                .fontSize(30)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.6F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();

        WatermarkUtils.addWatermark(param);

    }

    @Test
    public void excel() throws WatermarkException {

        File file = new File("C:\\tmp\\conventer\\6e881c6d88dca18e583470ebb25352cc9cf21afa3d5a56dee1758ccec632e716\\38abb538986b49ddb0b260fa959de4fd.xlsx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(40.0F)
                .fontSize(100)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.4F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();


        WatermarkUtils.addWatermark(param);


    }

    @Test
    public void word() throws WatermarkException {

        File file = new File("F:\\doc.doc");
        File imageFile = new File("F:\\4 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(20.0F)
                .fontSize(50)
//                .imageFile(imageFile)
//                .xMove(200)
                .alpha(0.5F)
//                .yMove(200)
                .bespread(Boolean.FALSE)
                .color(Color.red).build();

        WatermarkUtils.addWatermark(param);


    }

    @Test
    public void ppt() throws WatermarkException {

        File file = new File("F:\\测试\\demo1.pptx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(40.0F)
                .fontSize(100)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.1F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();


        WatermarkUtils.addWatermark(param);


    }







}
