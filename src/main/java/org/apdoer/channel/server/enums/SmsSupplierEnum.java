package org.apdoer.channel.server.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */
@AllArgsConstructor
@Getter
public enum SmsSupplierEnum {

	/**
	 *
	 */
	DH3T_CHINA("dh3tChina", "大汉三通"),
	/**
	 *
	 */
	DAHAN_CHINA("dahanChina","大汉三通sdk"),
	/**
	 * 阿里云中国
	 */
	ALIYUN_CHINA("aliyunChina", "阿里云中国"),
	/**
	 * 创蓝中国
	 */
	CHUANGLAN_CHINA("chuanglanChina","创蓝"),
	/**
	 * 创蓝国际
	 */
	CHUANGLAN_INTER("chuanglanInter","创蓝国际"),
	/**
	 * 赛邮中国
	 */
	SUBMAIL_CHINA("submailChina","赛邮"),
	/**
	 * 赛邮国际
	 */
	SUBMAIL_INTER("submailInter","赛邮"),
	/**
	 * 云片中国
	 */
	YP_CHINA("yunpianChina","云片中国"),
	/**
	 * 云片国际
	 */
	YP_INTER("yunpianInter","云片国际"),
	/**
	 * aws 中国
	 */
	AWS_CHINA("awsChina","aws中国");


	private String	code;
	private String	desc;


}
