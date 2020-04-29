package org.apdoer.channel.server.model.po;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "web_user_ext")
public class UserExtPo implements Serializable {

    private static final long serialVersionUID = -3668694758148002897L;

    private Integer userId;

    private Byte userType;

    private Byte nodeType;

    private Byte partnerType;

    private String countryCode;

    private String areaCode;

    private Long krwDailyLimit;

    private Date createTime;

    private Date updateTime;

}
