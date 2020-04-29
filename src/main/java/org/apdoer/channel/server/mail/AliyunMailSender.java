package org.apdoer.channel.server.mail;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.MailSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


@Slf4j
@Component("aliyunMailSender")
public class AliyunMailSender extends AbstractMsgSender {

	@Value("${msg.mail.supplier.aliyun.host}")
	private String host;

	@Value("${msg.mail.supplier.aliyun.port}")
	private String port;

	@Value("${msg.mail.supplier.aliyun.username}")
	private String username;

	@Value("${msg.mail.supplier.aliyun.password}")
	private String password;

	@Value("${msg.mail.supplier.aliyun.ssl.class}")
	private String sslClass;

	@Value("${msg.mail.supplier.aliyun.ssl.port}")
	private String sslPort;

	/**
	 * 发送邮件
	 * @param contact
	 * @param subject
	 * @param content
	 */
	private void sendHtmlMail(String contact, String subject, String content) {
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", sslPort);
		props.put("mail.smtp.port", port);
		props.put("mail.user", username);
		props.put("mail.password", password);
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession){
		};
		try {
			InternetAddress from = new InternetAddress(username);
			message.setFrom(from);
			InternetAddress to = new InternetAddress(contact);
			message.setRecipient(MimeMessage.RecipientType.TO, to);
			// 设置邮件标题
			message.setSubject(subject,"UTF-8");
			// 设置邮件的内容体
			message.setContent(content, "text/html;charset=UTF-8");
			Transport.send(message);
			log.info("阿里云邮件发送成功！");
		}
		catch (MessagingException e) {
			log.error("阿里云邮件发送失败！失败原因: "+e.getMessage());
		}
	}

	@Override
	public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
		this.sendHtmlMail( msgRecord.getContact(), msgRecord.getSubject(), msgRecord.getContent() );
	}

	@Override
	public String supplier() {
		return MailSupplierEnum.ALIYUN.getCode();
	}
}
