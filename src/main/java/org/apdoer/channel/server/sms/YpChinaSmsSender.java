package org.apdoer.channel.server.sms;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/22 14:25
 */
@Slf4j
@Component("ypChinaSmsSender")
public class YpChinaSmsSender extends AbstractMsgSender {


    @Autowired
    private YunpianClient clnt;

    @Override
    public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
        Map<String, String> param = buildSendData(msgRecord);
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        if (r.isSucc()) {
            log.info("发送成功,参数:{}", msgRecord.toString());
        } else {
            log.error("发送失败===,{},参数:{}", r.toString(), msgRecord.toString());
        }
    }

    private Map<String, String> buildSendData(MsgRecordPo msgRecord) {
        Map<String, String> param = new HashMap<>(16);
        param.put(YunpianClient.MOBILE, msgRecord.getContact());
        param.put(YunpianClient.TEXT, msgRecord.getContent());
        return param;
    }

    @Override
    public String supplier() {
        return SmsSupplierEnum.YP_CHINA.getCode();
    }
}
