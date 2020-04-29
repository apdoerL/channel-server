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
 * @author apdoer
 */
@Component("smsProvider")
public class SmsProvider implements MsgSenderProvider, InitializingBean, ApplicationContextAware {
    private List<SupplierRatio> supplierRatio = new CopyOnWriteArrayList<>();
    private ApplicationContext applicationContext;

    @Value("${msg.sms.supplier.internationale.ratio}")
    private String ratio;

    /**
     * 按配置比例分配渠道商</br>
     * 可优化：可按错误率动态分配</br>
     *
     * @param request
     * @return MsgSender
     */
    @Override
    public MsgSender select(final MsgerRequestDto request) {
        String supplierStr = ChannelUtils.getSupplier(request, supplierRatio);
        return applicationContext.getBean(supplierStr + "SmsSender", MsgSender.class);
    }

    /**
     * 提供http接口实时更改动态分配比例<br/>
     *
     * @param json
     * @author zdhf
     * @since JDK 1.8
     */
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
