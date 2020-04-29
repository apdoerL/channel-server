package org.apdoer.channel.server.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * InitializingBean 在类加载的时候将配置加载到内存中,通过afterPropertiesSet
 * @author apdoer
 */
@Component
public class CountryService implements InitializingBean {

    private static ConcurrentHashMap<String, CountryDto> countries = new ConcurrentHashMap<>();

    public String getTelephoneCode(String countryCode) {
        CountryDto countryDto = countries.get(countryCode);
        if (countryDto == null) {
            return null;
        }
        return countryDto.getTelephoneCode();
    }

    public Boolean isEnable(String countryCode) {
        CountryDto countryDto = countries.get(countryCode);
        if (countryDto == null) {
            return false;
        }
        return countryDto.getEnable();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:country.json");
        String json = IOUtils.toString(resource.getInputStream(), "UTF-8");

        List<CountryDto> list = JSON.parseArray(json, CountryDto.class);
        list.forEach(e -> countries.put(e.getId(), e));
    }


    /**
     * 非静态内部类实例化依赖于外部类,反序列化失败,静态则不会,所以这里用static
     */
    @Data
    private static class CountryDto {
        // 国家码,大写
        private String id;
        // 汉语
        private String chineseName;
        // 通用名称
        private String commonName;
        // 页面展示名称
        private String displayName;
        // 标准英文全称
        private String formalName;
        // 是否启用
        private Boolean enable;
        // 国际区号
        private String telephoneCode;
    }
}
