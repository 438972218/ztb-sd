package com.xdcplus.biz.common.config;

import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import plus.ojbk.minio.autoconfigure.properties.MinioProperties;
import plus.ojbk.minio.core.MinioTemplate;
import plus.ojbk.minio.util.MinioUtils;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MinioCustomTemplate{

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private MinioProperties properties;
    private String endpoint;
    private String bucket;
    private String accessKey;
    private String secretKey;
    public static final String URI_DELIMITER = "/";
    @Autowired
    private MinioClient minioClient;
    private MinioClient expandClient;

    public MinioCustomTemplate(MinioProperties properties) {
        this.properties = properties;
    }

    public MinioClient getMinioClient() {
        if (this.expandClient == null) {
            Class var1 = MinioTemplate.class;
            synchronized(MinioTemplate.class) {
                if (this.expandClient == null) {
                    this.expandClient = this.minioClient;
                }
            }
        }

        return this.expandClient;
    }

    @PostConstruct
    private void init() {
        this.endpoint = this.properties.getEndpoint();
        this.bucket = this.properties.getBucket();
        this.accessKey = this.properties.getAccessKey();
        this.secretKey = this.properties.getSecretKey();
        this.createBucket(this.bucket);
        this.log.info("Init Default Bucket :{} .", this.bucket);
    }

    public void createBucket(String bucket) {
        try {
            if (!this.minioClient.bucketExists((BucketExistsArgs)((BucketExistsArgs.Builder)BucketExistsArgs.builder().bucket(bucket)).build())) {
                this.minioClient.makeBucket((MakeBucketArgs)((io.minio.MakeBucketArgs.Builder)MakeBucketArgs.builder().bucket(bucket)).build());
            }

        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public InputStream getObjectInputStream(String bucket, String object) {
        try {
            return this.minioClient.getObject((GetObjectArgs)((io.minio.GetObjectArgs.Builder)((io.minio.GetObjectArgs.Builder)((io.minio.GetObjectArgs.Builder)((io.minio.GetObjectArgs.Builder)GetObjectArgs.builder().bucket(bucket)).object(object)).offset(0L)).length((Long)null)).build());
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public InputStream getObjectInputStream(String object) {
        return this.getObjectInputStream(this.bucket, object);
    }

    public String getObject(String bucket, String object, int duration, TimeUnit unit) {
        try {
            return this.minioClient.getPresignedObjectUrl((GetPresignedObjectUrlArgs)((io.minio.GetPresignedObjectUrlArgs.Builder)((io.minio.GetPresignedObjectUrlArgs.Builder)GetPresignedObjectUrlArgs.builder().method(Method.GET).expiry(duration, unit).bucket(bucket)).object(object)).build());
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public String getObject(String object) {
        return this.getObject(object, 1, TimeUnit.HOURS);
    }

    public String getObject(String object, int duration, TimeUnit unit) {
        return this.getObject(this.bucket, object, duration, unit);
    }

    public void deleteObject(String object) {
        this.deleteObject(this.bucket, object);
    }

    public void deleteObject(String bucket, String object) {
        try {
            this.minioClient.removeObject((RemoveObjectArgs)((io.minio.RemoveObjectArgs.Builder)((io.minio.RemoveObjectArgs.Builder)RemoveObjectArgs.builder().bucket(bucket)).object(object)).build());
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public ObjectWriteResponse putObject(String bucket, MultipartFile multipartFile) {
        try {
            String[] folders = MinioUtils.getDateFolder();
//            String fileName = MinioUtils.getUUID() + "." + MinioUtils.getSuffix(multipartFile.getOriginalFilename());
            String finalPath = String.join("/", folders) + "/" + multipartFile.getOriginalFilename();
            return this.minioClient.putObject((PutObjectArgs)((io.minio.PutObjectArgs.Builder)((io.minio.PutObjectArgs.Builder)PutObjectArgs.builder().stream(multipartFile.getInputStream(), multipartFile.getSize(), 5242880L).object(finalPath)).contentType(multipartFile.getContentType()).bucket(bucket)).build());
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public ObjectWriteResponse putObject(MultipartFile multipartFile) {
        return this.putObject(this.bucket, multipartFile);
    }

    public List<ObjectWriteResponse> putObject(String bucket, MultipartFile... multipartFiles) {
        try {
            List<ObjectWriteResponse> retVal = new ArrayList();
            String[] folders = MinioUtils.getDateFolder();
            MultipartFile[] var5 = multipartFiles;
            int var6 = multipartFiles.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                MultipartFile multipartFile = var5[var7];
                String fileName = MinioUtils.getUUID() + "." + MinioUtils.getSuffix(multipartFile.getOriginalFilename());
                String finalPath = String.join("/", folders) + "/" + fileName;
                ObjectWriteResponse response = this.minioClient.putObject((PutObjectArgs)((io.minio.PutObjectArgs.Builder)((io.minio.PutObjectArgs.Builder)PutObjectArgs.builder().stream(multipartFile.getInputStream(), multipartFile.getSize(), 5242880L).object(finalPath)).contentType(multipartFile.getContentType()).bucket(bucket)).build());
                retVal.add(response);
            }

            return retVal;
        } catch (Exception var12) {
            throw new RuntimeException(var12);
        }
    }

    public List<ObjectWriteResponse> putObject(MultipartFile... multipartFiles) {
        return this.putObject(this.bucket, multipartFiles);
    }

    public ObjectWriteResponse putObject(String bucket, String object, InputStream stream) {
        try {
            return this.minioClient.putObject((PutObjectArgs)((io.minio.PutObjectArgs.Builder)((io.minio.PutObjectArgs.Builder)PutObjectArgs.builder().stream(stream, -1L, 5242880L).object(object)).contentType("application/octet-stream").bucket(bucket)).build());
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }
}
