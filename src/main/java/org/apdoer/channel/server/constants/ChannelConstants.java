package org.apdoer.channel.server.constants;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/27 10:10
 */
public class ChannelConstants {

    /**
     * 邮件数量自增keypreffix
     */
    public static final String EMAIL_INCR_PREFFIX = "EMAIL_NUM_INCR_";

    /**
     * 邮件标记
     */
    public static final String EMAIL_FLAG = "@";

    /**
     * 实际等待发送的队列名称
     */
    public static final String CHANNEL_WAIT_SEND = "CHANNEL_WAIT_SEND";


    /**
     * 默认语言
     */
    public static final String USER_LOCALE = "zh_CN";

    /**
     * 默认地区码
     */
    public static final String DEFAULT_AREACODE = "+86";

    /**
     * 阿里云短信 正确码
     */
    public static final String ALIYUN_CODE_OK = "OK";

    /**
     * 创蓝正确码
     */
    public static final String CHUANGLAN_CODE_OK = "0";

    /**
     * submail 成功码
     */
    public static final String SUCCESS = "success";
}
