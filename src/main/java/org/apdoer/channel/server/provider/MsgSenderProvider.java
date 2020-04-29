package org.apdoer.channel.server.provider;


import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.sender.MsgSender;

/**
 * @author apdoer
 */
public interface MsgSenderProvider {


    /**
     * 根据发送的内容,地区,类型..确定具体的供应商发送器
     *  todo 这里应该再加上对应supplier的余额校验,超限制重选
     * @param request
     * @return
     */
    MsgSender select(final MsgerRequestDto request);
}
