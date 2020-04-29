package org.apdoer.channel.server.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */

@AllArgsConstructor
@Getter
public enum MailSupplierEnum {

	/**
	 * 阿里云邮件
	 */
	ALIYUN("aliyun", "阿里云"),
	/**
	 * aws 邮件
	 */
	AWS("aws","aws");


	private String	code;
	private String	desc;


}
