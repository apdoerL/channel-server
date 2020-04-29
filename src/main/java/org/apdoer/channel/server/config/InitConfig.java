//package org.apdoer.channel.server.config;
//
//import org.apdoer.channel.client.dto.MsgerRequestDto;
//import org.apdoer.channel.server.provider.MsgSenderProvider;
//import org.apdoer.channel.server.provider.MsgSenderProviderRouter;
//import org.apdoer.channel.server.sender.MsgSender;
//import org.apdoer.channel.server.service.impl.RedisOperateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author Li
// * @version 1.0
// * @date 2020/4/27 19:57
// */
//@Component
//public class InitConfig {
//    @Autowired
//    private RedisOperateService redisOperateService;
//    @Autowired
//    private MsgSenderProviderRouter msgSenderProviderRouter;
//
//
//    @PostConstruct
//    public void init(){
//      new Thread(new Runnablea(redisOperateService, msgSenderProviderRouter)).start();
//    }
//
//
//    private class Runnablea implements Runnable{
//
//        private RedisOperateService redisOperateService;
//        private MsgSenderProviderRouter msgSenderProviderRouter;
//
//
//        public Runnablea(RedisOperateService redisOperateService, MsgSenderProviderRouter msgSenderProviderRouter) {
//            this.redisOperateService = redisOperateService;
//            this.msgSenderProviderRouter = msgSenderProviderRouter;
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    MsgerRequestDto requestDto = this.redisOperateService.rightPop();
//                    if (null != requestDto) {
//                        ThreadPool.getInstance().execute(new SendRunnable(msgSenderProviderRouter,requestDto));
//                    } else {
//                        Thread.sleep(100L);
//                    }
//                } catch (Exception e) {
//                }
//            }
//        }
//    }
//
//
//
//    private class SendRunnable implements Runnable {
//
//        private MsgSenderProviderRouter msgSenderProviderRouter;
//        private MsgerRequestDto requestDto;
//
//        SendRunnable(MsgSenderProviderRouter msgSenderProviderRouter, MsgerRequestDto requestDto) {
//            this.msgSenderProviderRouter = msgSenderProviderRouter;
//            this.requestDto = requestDto;
//        }
//
//        @Override
//        public void run() {
//            MsgSenderProvider provider = msgSenderProviderRouter.forProvider(requestDto.getContact(), requestDto.getAreaCode());
//            MsgSender sender = provider.select(requestDto);
//            sender.send(requestDto);
//        }
//    }
//}
