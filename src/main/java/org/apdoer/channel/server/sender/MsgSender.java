package org.apdoer.channel.server.sender;

import org.apdoer.channel.client.dto.MsgerRequestDto;

/**
 * @author apdoer
 */
public interface MsgSender {


    /**
     * 发送
     * @param request
     */
    void send(final MsgerRequestDto request);
}
