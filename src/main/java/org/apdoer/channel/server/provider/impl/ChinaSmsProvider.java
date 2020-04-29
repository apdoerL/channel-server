package org.apdoer.channel.server.provider.impl;

import com.alibaba.fastjson.JSON;
import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.provider.MsgSenderProvider;
import org.apdoer.channel.server.sender.MsgSender;
import org.apdoer.channel.server.model.dto.SupplierRatio;
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
 * InitializingBean,在bean实例化的时候,将参数读取到内存
 * ApplicationContextAware 获取spring上下文,通过名称获取bean
 * CopyOnWriteArrayList 读写list,适用于读多写少的场景,concurrenthashmap也可
 * @author apdoer
 */
@Component("chinaSmsProvider")
public class ChinaSmsProvider implements MsgSenderProvider, InitializingBean, ApplicationContextAware {
    private List<SupplierRatio> supplierRatio = new CopyOnWriteArrayList<>();
    private ApplicationContext applicationContext;

    @Value("${msg.sms.supplier.china.ratio}")
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
        return applicationContext.getBean(supplierStr + "ChinaSmsSender", MsgSender.class);
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
