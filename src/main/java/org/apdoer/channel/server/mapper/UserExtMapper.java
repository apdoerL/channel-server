package org.apdoer.channel.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.channel.server.model.po.UserExtPo;
import org.apdoer.common.service.common.BaseMapper;

public interface UserExtMapper extends BaseMapper<UserExtPo> {

    UserExtPo queryUserExtByContact(@Param("contact") String contact);

}
