package org.apdoer.channel.server.config;

import config.AppConfig;
import lib.Internationalsms;
import lib.Message;
import lib.base.ISender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/17 18:59
 */
@Configuration
public class SubmailSmsConfiguration {

    @Value("${msg.sms.supplier.china.submail.appId}")
    private String applId;
    @Value("${msg.sms.supplier.china.submail.appKey}")
    private String appKey;

    @Value("${msg.sms.supplier.internationale.submail.appId}")
    private String interApplId;
    @Value("${msg.sms.supplier.internationale.submail.appKey}")
    private String interAppKey;

    @Bean("submailSender")
    public ISender submailSender() {
        AppConfig appConfig = new AppConfig();
        appConfig.setAppId(applId);
        appConfig.setAppKey(appKey);
        return new Message(appConfig);
    }

    @Bean("submailInternationaleSender")
    public ISender submailInternationaleSender() {
        AppConfig appConfig = new AppConfig();
        appConfig.setAppId(interApplId);
        appConfig.setAppKey(interAppKey);
        return new Internationalsms(appConfig);
    }
}
