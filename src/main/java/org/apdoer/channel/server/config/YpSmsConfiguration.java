package org.apdoer.channel.server.config;

import com.yunpian.sdk.YunpianClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/23 11:52
 */
@Configuration
public class YpSmsConfiguration {

    @Value("${msg.sms.supplier.china.yunpian.apikey}")
    private String apikey;

    @Bean("clnt")
    public YunpianClient getClnt() {
        return new YunpianClient(apikey).init();
    }

}
