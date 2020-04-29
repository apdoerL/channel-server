package org.apdoer.channel.server.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.apdoer.channel.server.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apdoer.channel.server.constants.ChannelConstants.CHUANGLAN_CODE_OK;

/**
 * 短信供应商-创蓝国际
 */
@Slf4j
@Component("chuanglanSmsSender")
public class ChuanglanSmsSender extends AbstractMsgSender {

    @Value("${msg.sms.supplier.internationale.chuanglan.url}")
    private String url;

    @Value("${msg.sms.supplier.internationale.chuanglan.account}")
    private String account;

    @Value("${msg.sms.supplier.internationale.chuanglan.password}")
    private String password;

    @Override
    public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
        String buildParams = this.buildChuanglanParam(msgRecord);
        log.info("创蓝-发送短信请求参数为:" + buildParams);
        try {
            String result = HttpUtils.post(url, buildParams, "UTF-8");
            log.info("创蓝-发送短信返回参数为:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.get("code").toString();
            if (CHUANGLAN_CODE_OK.equalsIgnoreCase(code)) {
                log.info("创蓝-短信发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String supplier() {
        return SmsSupplierEnum.CHUANGLAN_INTER.getCode();
    }

    private String buildChuanglanParam(MsgRecordPo msgRecord) {
        JSONObject map = new JSONObject();
        map.put("account", account);
        map.put("password", password);
        map.put("msg", msgRecord.getContent());
        map.put("mobile", msgRecord.getAreaCode() + msgRecord.getContact());
        return map.toString();
    }
}
