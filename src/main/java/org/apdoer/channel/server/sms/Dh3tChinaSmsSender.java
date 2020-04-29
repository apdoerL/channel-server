package org.apdoer.channel.server.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.entity.ContentType;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.mapper.MsgRecordMapper;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.model.vo.HttpResult;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.apdoer.channel.server.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;


/**
 * @author apdoer
 */
@Slf4j
@Component("dh3tChinaSmsSender")
public class Dh3tChinaSmsSender extends AbstractMsgSender {

	private static final String	URL	= "http://www.dh3t.com/json/sms/Submit";

	@Value("${msg.sms.supplier.china.dh3t.account}")
	private String				account;
	@Value("${msg.sms.supplier.china.dh3t.password}")
	private String				password;
	@Value("${msg.sms.supplier.china.dh3t.sign}")
	private String				sign;
	@Autowired
	private MsgRecordMapper msgRecordMapper;

	@Override
	public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
		// 构造发送参数
		String json = this.buildRequest( msgRecord );
		try {
			HttpResult hr = HttpUtils.doPost( URL, json, ContentType.APPLICATION_JSON );
			log.info( "dh3t.sms,request={},result={}", json, hr.getContent() );
			if ("200".equals( hr.getStatusCode() )) {
				JSONObject map = JSON.parseObject( hr.getContent() );
				String result = map.getString( "result" );
				if ("0".equals( result )) {
					String msgid = map.getString( "msgid" );
					msgRecordMapper.update2Sended( msgRecord.getId(), hr.getContent(), msgid );
					return;
				}
			}
			msgRecordMapper.update2Failure( msgRecord.getId(), hr.getContent() );
		} catch (IOException e) {
			log.error( "dh3t.send.io.exception,msgRecord={}", msgRecord.getReqId(), e );
		}
	}

	private String buildRequest(MsgRecordPo msgRecord) {
		JSONObject json = new JSONObject();
		json.put( "account", account );
		json.put( "password", password );
		json.put( "msgid", msgRecord.getReqId() );
		json.put( "phones", "+" + msgRecord.getAreaCode() + msgRecord.getContact() );
		json.put( "content", msgRecord.getContent() );
		json.put( "sign", sign );
		json.put( "sendtime", DateFormatUtils.format( new Date(), "yyyyMMddHHmm" ) );
		return json.toJSONString();
	}

	@Override
	public String supplier() {
		return SmsSupplierEnum.DH3T_CHINA.getCode();
	}

}
