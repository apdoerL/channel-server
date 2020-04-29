package org.apdoer.channel.server.sms;

import lib.base.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.common.service.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apdoer.channel.server.constants.ChannelConstants.SUCCESS;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/17 18:14
 */
@Slf4j
@Component("submailSmsSender")
public class SubmailSmsSender extends AbstractSubmailSmsSender {


    @Value("${msg.sms.supplier.internationale.submail.appId}")
    private String interApplId;
    @Value("${msg.sms.supplier.internationale.submail.appKey}")
    private String interAppKey;
    @Value("${msg.sms.supplier.internationale.submail.sign}")
    private String interSign;

    @Autowired
    private ISender submailInternationaleSender;

    @Override
    public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
        Map<String, Object> data = new HashMap<>(16);
        try {
            data = buildSubmailMsg(msgRecord);
            log.info("submail 发送短信开始====,参数:{}", data.toString());
            String response = submailInternationaleSender.send(data);
            log.info(response);
            ResponseDto dto = JsonUtils.toObject(response, ResponseDto.class);
            if (SUCCESS.equalsIgnoreCase(dto.getStatus())) {
                log.info("submail 发送短信成功,参数:{}", data.toString());
            } else {
                log.error("submail 发送短信失败,参数:{}", data.toString());
            }
        } catch (Exception e) {
            log.error("submail 发送短信失败,参数,{}.原因:{}", data.toString(), e);
        }
    }


    @Override
    Map<String, Object> buildSubmailMsg(MsgRecordPo msgRecord) {
        return doBuildMsg(interApplId, interAppKey, msgRecord.getAreaCode() + msgRecord.getContact(), msgRecord.getContent(), interSign);
    }

    @Override
    public String supplier() {
        return SmsSupplierEnum.SUBMAIL_INTER.getCode();
    }


}
