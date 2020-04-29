package org.apdoer.channel.server.service.impl;

import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.common.service.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static org.apdoer.channel.server.constants.ChannelConstants.CHANNEL_WAIT_SEND;

/**
 * @author apdoer
 */
@Component
public class RedisOperateService {
	

	
	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
	
    public void leftPush(MsgerRequestDto value) {
    	String valueStr = JsonUtils.toJson(value);
    	this.redisTemplate.opsForList().leftPush(CHANNEL_WAIT_SEND, valueStr);
    }
    
    public MsgerRequestDto rightPop() {
    	Object valueStr = this.redisTemplate.opsForList().rightPop(CHANNEL_WAIT_SEND);
    	if (null != valueStr) {
    		return JsonUtils.toObject(valueStr.toString(), MsgerRequestDto.class);
    	} else {
    		return null;
    	}
    }
    
    public Long size() {
    	return this.redisTemplate.opsForList().size(CHANNEL_WAIT_SEND);
    }

}