package org.apdoer.channel.server.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/27 10:53
 */
@Slf4j
public class ChannelThreadFactory {
    private ThreadFactory threadFactory;

    private ChannelThreadFactory() {
        this.threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("thread-channel-server-%d")
                .setUncaughtExceptionHandler(new ChannelUnCaughExceptionHandler()).build();
    }

    private static class InnerThreadFactory {
        private static final ChannelThreadFactory INSTANCE = new ChannelThreadFactory();
    }

    public static ThreadFactory getInstance() {
        return InnerThreadFactory.INSTANCE.threadFactory;
    }


    private class ChannelUnCaughExceptionHandler implements Thread.UncaughtExceptionHandler{

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error("thread:{}.exception:{}",t.getName(),e.getMessage());
        }
    }
}
