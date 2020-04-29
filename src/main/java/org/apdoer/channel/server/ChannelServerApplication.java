package org.apdoer.channel.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *< @EnableCaching 开启缓存
 * <@EnableDiscoveryClient 注册到注册中心
 * @author apdoer
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class})
@EnableDiscoveryClient
@EnableCaching
public class ChannelServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChannelServerApplication.class, args);
    }

}
