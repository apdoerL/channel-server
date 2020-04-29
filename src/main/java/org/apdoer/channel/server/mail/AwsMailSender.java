package org.apdoer.channel.server.mail;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.enums.MailSupplierEnum;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.sender.AbstractMsgSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


/**
 * @author apdoer
 */
@Slf4j
@Component("awsMailSender")
public class AwsMailSender extends AbstractMsgSender {

    @Value("${msg.mail.supplier.aws.from}")
    private String from;

    @Value("${msg.mail.supplier.aws.smtpUsername}")
    private String username;

    @Value("${msg.mail.supplier.aws.smtpPassword}")
    private String password;

    @Value("${msg.mail.supplier.aws.host}")
    private String host;

    @Value("${msg.mail.supplier.aws.port}")
    private int port;

    @Override
    public String supplier() {
        return MailSupplierEnum.AWS.getCode();
    }

    @Override
    public void doSend(MsgRecordPo msgRecord, Map<String, String> params) {
        this.sendMail(msgRecord, params);
    }

    private void sendMail(MsgRecordPo msgRecord, Map<String, String> params) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(msgRecord.getContact()));
            msg.setSubject(msgRecord.getSubject(), "UTF-8");
            msg.setContent(msgRecord.getContent(), "text/html;charset=UTF-8");
            Transport transport = session.getTransport();
            log.info("邮件发送中...");
            transport.connect(host, username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("邮件发送成功!");
            transport.close();
        } catch (Exception ex) {
            log.error("邮件发送失败！失败原因: " + ex.getMessage());
        }

    }

}
