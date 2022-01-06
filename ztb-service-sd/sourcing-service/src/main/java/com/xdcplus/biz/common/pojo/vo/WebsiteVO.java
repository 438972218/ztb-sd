package com.xdcplus.biz.common.pojo.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties(prefix ="website" )
public class WebsiteVO implements Serializable {

    private static final long serialVersionUID = 1265075030465496888L;

    private String platFromName;

    private String vendorPlatFromName;
}
