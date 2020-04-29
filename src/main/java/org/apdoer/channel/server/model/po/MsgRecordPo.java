package org.apdoer.channel.server.model.po;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author apdoer
 */
@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgRecordPo implements Serializable {
    private static final long serialVersionUID = -7668227626892869889L;

    private Long id;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 模板
     */
    private String templateCode;
    /**
     * 语言
     */
    private String locale;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 标题
     */
    private String subject;
    /**
     * 内容
     */
    private String content;
    /**
     * 响应内容
     */
    private String responseText;
    /**
     * 请求id
     */
    private String reqId;
    /**
     * 响应id
     */
    private String resId;

    private String remark;
    /**
     * 客户ip
     */
    private String ip;
    /**
     * 状态
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
    /**
     * 区号
     */
    private String areaCode;


}
