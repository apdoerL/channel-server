package org.apdoer.channel.server.sender.impl;


import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.sender.MsgSender;
import org.apdoer.channel.server.service.impl.RedisOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author apdoer
 */
@Component("msgSenderDelegator")
public class MsgSenderDelegator implements MsgSender {


    @Autowired
    private RedisOperateService redisOperateService;


    @Override
    public void send(MsgerRequestDto request) {
        this.redisOperateService.leftPush(request);
    }

}
