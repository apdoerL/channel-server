package org.apdoer.channel.server.sms;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.SmsSupplierEnum;
import org.apdoer.channel.server.mapper.MsgRecordMapper;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author apdoer
 */
@Slf4j
@Component("awsChinaSmsSender")
public class AwsChinaSmsSender extends AbstractMsgSender {
	
	@Value("${msg.sms.supplier.china.aws.accessKeyId}")
	private String		accessKeyId;
	
	@Value("${msg.sms.supplier.china.aws.accessKeySecret}")
	private String		accessKeySecret;
	
	@Autowired
	private MsgRecordMapper msgRecordMapper;
	
	private static final String DEFAULT_SMS_SENDERID = "chainex";
	private static final String DEFAULT_SMS_MAXPRICE = "0.05";
	private static final String DEFAULT_SMS_SMSTYPE  = "Promotional";
	
	private Map<String, MessageAttributeValue> smsAttributes;
	
	private Map<String, MessageAttributeValue> getDefaultSMSAttributes() {
		 if (smsAttributes == null) {
			 smsAttributes = new HashMap<>();
			 smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(DEFAULT_SMS_SENDERID).withDataType("String"));
			 smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue(DEFAULT_SMS_MAXPRICE).withDataType("Number"));
			 smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue(DEFAULT_SMS_SMSTYPE).withDataType("String"));
		 }
		 return smsAttributes;
	}
	
	
	private Map<String,String> buildRequest(MsgRecordPo msgRecord) {
		Map<String,String> map = new HashMap<String,String>();
		map.put( "phone", "+" + msgRecord.getAreaCode() + msgRecord.getContact());
		map.put( "content", msgRecord.getContent() );
		return map;
	}
	
	@Override
	public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, accessKeySecret);
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
								.withRegion("us-east-1")
								.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .build();
		Map<String,String> map = this.buildRequest(msgRecord);
		
        String message = map.get("content");
        String phoneNumber = map.get("phone");
        Map<String, MessageAttributeValue> smsAttributes =
                this.getDefaultSMSAttributes();
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
		if(result != null) {
			String msgid = result.getMessageId();
			msgRecordMapper.update2Sended( msgRecord.getId(), msgid, msgid );
			return;
		}else {
			msgRecordMapper.update2Failure( msgRecord.getId(), "");
		}
	}

	@Override
	public String supplier() {
		return SmsSupplierEnum.AWS_CHINA.getCode();
	}

}
