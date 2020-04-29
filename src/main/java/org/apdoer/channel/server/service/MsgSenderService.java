package org.apdoer.channel.server.service;

import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.common.service.model.vo.ResultVo;

import java.util.List;

public interface MsgSenderService {


    /**
     * 发送,进redis队列,不是实际发送
     * @param msgerRequestDto
     * @return
     */
    ResultVo msgSend(MsgerRequestDto msgerRequestDto);


    /**
     * 批量发送
     * @param msgList
     * @return
     */
    ResultVo msgBatchSend(List<MsgerRequestDto> msgList);
}
