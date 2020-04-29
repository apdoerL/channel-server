
package org.apdoer.channel.server.controller;


import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.service.MsgSenderService;
import org.apdoer.common.service.model.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author apdoer
 */
@RestController
public class MsgSenderController {

    @Autowired
    private MsgSenderService msgSenderService;



    @PostMapping("/msg/send")
    public ResultVo send(@RequestBody MsgerRequestDto request) {
        return msgSenderService.msgSend(request);

    }


    @GetMapping("/msg/hello")
    public String send() {
        return "SSSSS";

    }


    @PostMapping("/msg/sendBatch")
    public ResultVo sendBatch(@RequestBody List<MsgerRequestDto> msgList) {
        return msgSenderService.msgBatchSend(msgList);
    }

}
