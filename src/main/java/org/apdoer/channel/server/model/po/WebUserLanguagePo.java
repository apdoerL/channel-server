package org.apdoer.channel.server.model.po;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author apdoer
 */
@Table(name = "web_user_language")
@Data
@Builder
@ToString
public class WebUserLanguagePo implements Serializable {
    private static final long serialVersionUID = -1389215266449168524L;
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 语言_国家,zh_CN
     */
    private String locale;

}