package org.apdoer.channel.server.service;

import org.apdoer.channel.server.model.po.WebUserLanguagePo;

public interface UserLanguageService {
    /**
     * 根据用户联系方式查询用户语言
     */
    WebUserLanguagePo queryUserLanguageByContact(String contact);
}
