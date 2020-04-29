package org.apdoer.channel.server.provider;

import org.apdoer.channel.server.constants.ChannelConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 实现ApplicationContextAware 接口,是为了获取spring上下文,从而可以通过bean名称获取对应的bean
 * @author apdoer
 */
@Component
public class MsgSenderProviderRouter implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private static final List<String> AREA_CODE_CHINA = Arrays.asList("+86", "86");


    public MsgSenderProvider forProvider(String contact, String areaCode) {
        if (contact.contains(ChannelConstants.EMAIL_FLAG)) {
            return applicationContext.getBean("mailProvider", MsgSenderProvider.class);
        } else {
            if (Objects.isNull(areaCode) || AREA_CODE_CHINA.contains(areaCode)) {
                return applicationContext.getBean("chinaSmsProvider", MsgSenderProvider.class);
            } else {
                return applicationContext.getBean("smsProvider", MsgSenderProvider.class);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
