package org.apdoer.channel.server.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.check.ParamCheckService;
import org.apdoer.channel.server.code.ExceptionCode;
import org.apdoer.channel.server.model.po.WebUserLanguagePo;
import org.apdoer.channel.server.sender.MsgSender;
import org.apdoer.channel.server.service.MsgSenderService;
import org.apdoer.channel.server.service.UserLanguageService;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Locale;

import static org.apdoer.channel.server.constants.ChannelConstants.DEFAULT_AREACODE;
import static org.apdoer.channel.server.constants.ChannelConstants.USER_LOCALE;


/**
 * @author apdoer
 */
@Slf4j
@Component
public class MsgSenderServiceImpl implements MsgSenderService {

    @Autowired
    private CountryService countryService;

    @Autowired
    @Qualifier("msgSenderDelegator")
    private MsgSender msgSender;

    @Autowired(required = false)
    private UserLanguageService userLanguageService;

    @Autowired
    private ParamCheckService paramCheckService;

    @Override
    public ResultVo msgSend(MsgerRequestDto request) {
        //1.用户黑名单拦截
        boolean checkBlack = paramCheckService.checkBlackList(request.getContact());
        if (!checkBlack) {
            log.info("短信发送失败！用户{}已加入黑名单！", request.getContact());
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.SUCCESS.getCodeNo(), "该用户已加入黑名单！");
        }
        try {
            //2.参数校验
            ResultVo checkResultVo = this.requestParamCkeck(request);
            if (checkResultVo.getCode() != ExceptionCode.SUCCESS.getCodeNo()) {
                return checkResultVo;
            }
            //3.获取语言
            ResultVo localeResultVo = this.getLocale(request);
            if (localeResultVo.getCode() != ExceptionCode.SUCCESS.getCodeNo()) {
                return localeResultVo;
            }
            //4.消息发送
            msgSender.send(request);
            //5.邮件消息记数
            this.sendCountCalculate(request);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.SUCCESS.getCodeNo(), ExceptionCode.SUCCESS.getCodeName());
        } catch (Exception e) {
            log.error("渠道服务发送失败", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CHANNEL_SERVICE_DELIVERY_FAILURE.getCodeNo(), ExceptionCode.CHANNEL_SERVICE_DELIVERY_FAILURE.getCodeName());
        }
    }

    @Override
    public ResultVo msgBatchSend(List<MsgerRequestDto> msgList) {
        //todo 具体实现
        return null;
    }


    private ResultVo requestParamCkeck(MsgerRequestDto request) {
        if (StringUtils.isBlank(request.getContact())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTACT_NUST_BE_NOT_BLANK.getCodeNo(), ExceptionCode.CONTACT_NUST_BE_NOT_BLANK.getCodeName());
        }
        if (StringUtils.isBlank(request.getTemplate())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TEMPLATE_MUST_BE_NOT_BLANK.getCodeNo(), ExceptionCode.TEMPLATE_MUST_BE_NOT_BLANK.getCodeName());
        }
        if (StringUtils.isBlank(request.getIp())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.IP_MUST_BE_NOT_BLANK.getCodeNo(), ExceptionCode.IP_MUST_BE_NOT_BLANK.getCodeName());
        }
        return ResultVoBuildUtils.buildResultVo(ExceptionCode.SUCCESS.getCodeNo(), ExceptionCode.SUCCESS.getCodeName());
    }


    private ResultVo getLocale(MsgerRequestDto dto) {
        Locale locale;
        if (StringUtils.isBlank(dto.getLocale())) {
            WebUserLanguagePo userLanguagePo = userLanguageService.queryUserLanguageByContact(dto.getContact());
            if (userLanguagePo == null) {
                locale = LocaleUtils.toLocale(USER_LOCALE);
                dto.setLocale(locale.toString());
            } else {
                locale = LocaleUtils.toLocale(userLanguagePo.getLocale());
                dto.setLocale(userLanguagePo.getLocale());
            }
        } else {
            locale = LocaleUtils.toLocale(dto.getLocale());
        }

        if (StringUtils.isBlank(dto.getAreaCode())) {
            dto.setAreaCode(DEFAULT_AREACODE);
        }

        if (StringUtils.isBlank(locale.getLanguage()) || StringUtils.isBlank(locale.getCountry())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LOCALE_MUST_BE_NOT_BLANK.getCodeNo(), ExceptionCode.LOCALE_MUST_BE_NOT_BLANK.getCodeName());
        }
        if (!countryService.isEnable(locale.getCountry())) {
            // 不支持发送短信到该国
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.THIS_REGION_IS_NOT_SUPPORTED.getCodeNo(), locale.getCountry() + ExceptionCode.THIS_REGION_IS_NOT_SUPPORTED.getCodeName());
        }
        return ResultVoBuildUtils.buildResultVo(ExceptionCode.SUCCESS.getCodeNo(), ExceptionCode.SUCCESS.getCodeName());
    }


    private void sendCountCalculate(MsgerRequestDto dto) {
        // TODO SEND COUNT
    }

}
