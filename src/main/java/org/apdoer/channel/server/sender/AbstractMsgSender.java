package org.apdoer.channel.server.sender;


import org.antlr.stringtemplate.StringTemplate;
import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.mapper.MsgRecordMapper;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.channel.server.model.po.MsgTemplatePo;
import org.apdoer.channel.server.service.MsgTemplateService;
import org.apdoer.channel.server.service.impl.CountryService;
import org.apdoer.channel.server.supplier.MsgSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 抽象方法，交由各厂商发送接口发送<br/>
 *
 * @author apdoer
 * @since JDK 1.8
 */
public abstract class AbstractMsgSender implements MsgSender, MsgSupplier {

    @Value("${msg.mock}")
    private Boolean mock;

    @Autowired
    protected CountryService countryService;

    @Autowired
    protected MsgRecordMapper msgRecordMapper;

    @Autowired
    protected MsgTemplateService msgTemplateService;

    @Override
    public void send(MsgerRequestDto request) {
        MsgTemplatePo template = msgTemplateService.getTemplate(request.getTemplate(), request.getLocale());
        if (template == null) {
            throw new RuntimeException(request.getTemplate() + "_" + request.getLocale() + " does not exist in redis,plz check again");
        }
        // 构造MsgRecord
        MsgRecordPo msgRecord = this.buildMsgRecord(request, template);
        // 插入消息
        this.beforeSend(msgRecord);
        // mock模式不实际发邮件和短信，但是插入发送记录
        if (mock) {
            return;
        }
        // 调用供应商接口
        this.doSend(msgRecord, request.getParams());
    }

    protected MsgRecordPo buildMsgRecord(MsgerRequestDto request, MsgTemplatePo template) {
        // 替换标题参数
        StringTemplate subjectSt = new StringTemplate(template.getSubject());
        if (request.getParams() != null) {
            request.getParams().forEach(subjectSt::setAttribute);
        }
        // 替换body参数
        StringTemplate contentSt = new StringTemplate(template.getContent());
        if (request.getParams() != null) {
            request.getParams().forEach(contentSt::setAttribute);
        }
        // reqId
        String uuid = UUID.randomUUID().toString();
        // 构造消息对象
        MsgRecordPo record = new MsgRecordPo();
        record.setContact(request.getContact());
        record.setTemplateCode(template.getTemplateCode());
        record.setLocale(request.getLocale());
        record.setReqId(uuid);
        record.setSupplier(this.supplier());
        record.setSubject(subjectSt.toString());
        record.setContent(contentSt.toString());
        record.setIp(request.getIp());
        record.setStatus(0);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setAreaCode(request.getAreaCode());
        return record;
    }


    protected void beforeSend(MsgRecordPo msgRecord) {
        int rows = msgRecordMapper.insert(msgRecord);
        if (rows != 1) {
            throw new IllegalArgumentException("MsgRecord fail to insert,rows=" + rows);
        }
    }

    public abstract void doSend(MsgRecordPo msgRecord, Map<String, String> params);

}
