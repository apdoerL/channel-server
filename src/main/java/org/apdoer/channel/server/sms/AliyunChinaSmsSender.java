package org.apdoer.channel.server.sms;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apdoer.channel.server.constants.ChannelConstants.ALIYUN_CODE_OK;

/**
 * @author apdoer
 */
@Slf4j
@Component("aliyunChinaSmsSender")
public class AliyunChinaSmsSender extends AbstractMsgSender {

    @Value("${msg.sms.supplier.china.aliyun.signName}")
    private String signName;

    @Autowired
    private IAcsClient acsClient;

    @Override
    public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
        try {
            SendSmsRequest request = this.buildSendSmsRequest(msgRecord, params);
            log.info("aliyun.sms.request={}", JSON.toJSONString(request));
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            this.afterSend(msgRecord, sendSmsResponse);
        } catch (ClientException e) {
            msgRecordMapper.update2Exception(msgRecord.getId(), "异常");
            log.error("阿里云短信发送异常error when getAcsResponse for {}", msgRecord, e);
        }
    }

    private SendSmsRequest buildSendSmsRequest(MsgRecordPo msgRecord, Map<String, String> params) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(msgRecord.getAreaCode() + msgRecord.getContact());
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(msgRecord.getTemplateCode());
        request.setTemplateParam(JSON.toJSONString(params));
        return request;
    }

    public void afterSend(MsgRecordPo msgRecord, SendSmsResponse sendSmsResponse) {
        if (null == sendSmsResponse) {
            this.msgRecordMapper.update2Failure(msgRecord.getId(), "返回为null");
            return;
        }
        String result = JSON.toJSONString(sendSmsResponse);
        log.info("aliyun.sms.result={}", result);
        if (ALIYUN_CODE_OK.equalsIgnoreCase(sendSmsResponse.getCode())) {
            this.msgRecordMapper.update2Sended4Ali(msgRecord.getId(), result, sendSmsResponse.getRequestId(), sendSmsResponse.getBizId());
        } else {
            this.msgRecordMapper.update2Failure(msgRecord.getId(), result);
        }
    }

    @Override
    public String supplier() {
        return SmsSupplierEnum.ALIYUN_CHINA.getCode();
    }
}
