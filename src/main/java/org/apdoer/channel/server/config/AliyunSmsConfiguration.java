package org.apdoer.channel.server.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信配置
 * @author apdoer
 */
@Slf4j
@Configuration
public class AliyunSmsConfiguration {
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String REGION_ID = "cn-hangzhou";
    private static final String ENDPOINT_NAME = "cn-hangzhou";

    private static final String DEFAULT_CONNECT_TIMEOUT = "10000";
    private static final String DEFAULT_READ_TIMEOUT = "10000";

    static {
        System.setProperty("sun.net.client.defaultConnectTimeout", DEFAULT_CONNECT_TIMEOUT);
        System.setProperty("sun.net.client.defaultReadTimeout", DEFAULT_READ_TIMEOUT);
    }

    @Value("${msg.sms.supplier.china.aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${msg.sms.supplier.china.aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Bean
    public IAcsClient acsClient() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint(ENDPOINT_NAME, REGION_ID, PRODUCT, DOMAIN);
        } catch (ClientException e) {
            log.error("aliyun.sms.addEndpoint failure", e);
        }
        return new DefaultAcsClient(profile);
    }
}
