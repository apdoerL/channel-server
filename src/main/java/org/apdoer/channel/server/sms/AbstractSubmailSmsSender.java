package org.apdoer.channel.server.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/17 18:14
 */
@Slf4j
public abstract class AbstractSubmailSmsSender extends AbstractMsgSender {


    Map<String, Object> doBuildMsg(String appId, String appKey, String contact, String content, String sign) {
        if (!content.contains(sign)) {
            content = sign + content;
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("appid", appId);
        map.put("to", contact);
        map.put("content", content);
        map.put("signature", appKey);
        return map;
    }


    /**
     * 构建发送参数
     *
     * @param msgRecord
     * @return
     */
    abstract Map<String, Object> buildSubmailMsg(MsgRecordPo msgRecord);

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class ResponseDto {
        private String status;
        private String sendId;
        private BigDecimal fee;
        private String smsCredits;
        private String transactionalSmsCredits;
    }
}
