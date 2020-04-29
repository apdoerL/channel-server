package org.apdoer.channel.server.service;


import org.apdoer.channel.server.mapper.MsgTemplateMapper;
import org.apdoer.channel.server.model.po.MsgTemplatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgTemplateService {

	private static final String	UNDER_LINE	= "_";

	@Autowired
	private MsgTemplateMapper msgTemplateMapper;
	
	@Autowired
	private MsgTemplateService msgTemplateService;


	public MsgTemplatePo getTemplate(String bussinessType, String locale) {
		String tempKey = bussinessType + UNDER_LINE + locale;
		return msgTemplateService.getAllTemplat().get(tempKey);
	}
	
	@Cacheable(value = "COMMON",key = "'COMMON_msger_msg_templates'")
	public Map<String, MsgTemplatePo> getAllTemplat() {
		Map<String, MsgTemplatePo> map = new HashMap<>();
		List<MsgTemplatePo> temps = msgTemplateMapper.queryAllAvailableTemplates();
		if (null != temps) {
			for (MsgTemplatePo template : temps) {
				String tempKey = template.getBussinessType() + UNDER_LINE + template.getLocale();
				map.put(tempKey, template);
			}
		}
		return map;
	}
}
