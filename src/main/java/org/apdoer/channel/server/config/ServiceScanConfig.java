
package org.apdoer.channel.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * 扫描配置
 * @author apdoer
 */
@Configuration
@ComponentScan({
        "org.apdoer.common.service"
})
@MapperScan(basePackages = { "org.apdoer.channel.server.mapper"})
public class ServiceScanConfig {

}
