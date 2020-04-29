package org.apdoer.channel.server.config;

import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.provider.MsgSenderProvider;
import org.apdoer.channel.server.provider.MsgSenderProviderRouter;
import org.apdoer.channel.server.sender.MsgSender;
import org.apdoer.channel.server.service.impl.RedisOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * 用于在项目启动时执行一些任务,可以实现CommandLineRunner接口,多个任务可以指定执行顺序@Order(1)
 * 也可以通过配置bean @PostConstruct,在bean初始化的时候执行,
 * 还可以通过@ConditionalOnProperties 的afterPropertiesSet来实现
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/4/27 12:15
 */
@Configuration
public class InitCommandConfiguration {
    @Autowired
    private RedisOperateService redisOperateService;
    @Autowired
    private MsgSenderProviderRouter msgSenderProviderRouter;

    @Bean("readWaitSendCommnad")
    @Order(1000)
    public CommandLineRunner commandLineRunner() {
        return args -> new Thread(() -> {
            for (; ; ) {
                try {
//                    System.out.println("=====");
                    MsgerRequestDto requestDto = redisOperateService.rightPop();
                    if (Objects.nonNull(requestDto)) {
                        ThreadPool.getInstance().execute(new SendRunnable(msgSenderProviderRouter, requestDto));
                    } else {
                        Thread.sleep(100L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class SendRunnable implements Runnable {

        private MsgSenderProviderRouter msgSenderProviderRouter;
        private MsgerRequestDto requestDto;

        SendRunnable(MsgSenderProviderRouter msgSenderProviderRouter, MsgerRequestDto requestDto) {
            this.msgSenderProviderRouter = msgSenderProviderRouter;
            this.requestDto = requestDto;
        }

        @Override
        public void run() {
            MsgSenderProvider provider = msgSenderProviderRouter.forProvider(requestDto.getContact(), requestDto.getAreaCode());
            MsgSender sender = provider.select(requestDto);
            sender.send(requestDto);
        }
    }
}
