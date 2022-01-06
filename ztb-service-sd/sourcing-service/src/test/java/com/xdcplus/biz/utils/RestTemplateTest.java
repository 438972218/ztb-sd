package com.xdcplus.biz.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.biz.SourcingApplicationTest;
import com.xdcplus.biz.common.pojo.vo.BidVendorDetailVO;
import com.xdcplus.biz.service.BidVendorDetailService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RestTemplateTest extends SourcingApplicationTest {

    @Autowired
    MinioClient minioClient;

    @Autowired
    BidVendorDetailService bidVendorDetailService;

    @Test
    public void test1() {
        List<String> marks= CollectionUtil.newArrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V");
        String b =getMark(marks);
        System.out.println(b);
    }

    private String getMark(List<String> marks){
        while (true){
            char mark=(char)('A'+Math.random()*('Z'-'A'+1));
            String marka= String.valueOf(mark);
            if (!marks.contains(marka)){
                return marka;
            }
        }
    }


    @Test
    public void test2() {
        String b = getPresignedUrl("ztb", "74296f9773944dec8fb1ec6cea72310e.png");
        System.out.println(b);
    }

    public String getPresignedUrl(String bucketName, String attachmentNameInServer) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(attachmentNameInServer).build());
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(attachmentNameInServer).build());
        } catch (Exception e) {
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
    }



    @Test
    public void test3() {

        System.out.println("aa");
    }


}
