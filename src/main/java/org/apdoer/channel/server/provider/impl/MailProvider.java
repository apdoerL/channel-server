package org.apdoer.channel.server.provider.impl;

import com.alibaba.fastjson.JSON;

import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.model.dto.SupplierRatio;
import org.apdoer.channel.server.provider.MsgSenderProvider;
import org.apdoer.channel.server.sender.MsgSender;
import org.apdoer.channel.server.utils.ChannelUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 邮件暂时未做国外区分
 *
 * @author apdoer
 */
@Component("mailProvider")
public class MailProvider implements MsgSenderProvider, InitializingBean, ApplicationContextAware {

    private List<SupplierRatio> supplierRatio = new CopyOnWriteArrayList<>();
    private ApplicationContext applicationContext;

    /**
     * 权重 json格式 [{"supplier":"dh3t","weight":70},{"supplier":"ali","weight":70}]
     */
    @Value("${msg.mail.supplier.ratio}")
    private String ratio;

    @Override
    public MsgSender select(final MsgerRequestDto request) {
        String supplierStr = ChannelUtils.getSupplier(request, supplierRatio);
        return applicationContext.getBean(supplierStr + "MailSender", MsgSender.class);
    }


    public void refresh(String json) {
        castAndCache(json);
    }

    /**
     * value值转换并缓存
     */
    private void castAndCache(String ratioJson) {
        List<SupplierRatio> list = JSON.parseArray(ratioJson, SupplierRatio.class);
        supplierRatio.addAll(list);
    }

    @Override
    public void afterPropertiesSet() {
        castAndCache(ratio);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
