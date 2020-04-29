package org.apdoer.channel.server.mapper;

import org.apdoer.channel.server.model.po.MsgTemplatePo;
import org.apdoer.common.service.common.BaseMapper;

import java.util.List;

/**
 * @author apdoer
 */
public interface MsgTemplateMapper extends BaseMapper<MsgTemplatePo> {

    /**
     * 查询模板,所有
     * @return
     */
    List<MsgTemplatePo> queryAllAvailableTemplates();

}
