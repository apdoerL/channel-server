package org.apdoer.channel.server.code;

import java.util.*;

/**
 * 异常错误码枚举   0400-0499：服务响应 0500-0699：请求参数验证 0700-0899：业务验证
 */
public enum ExceptionCode {
	/**
	 * 0400-0499：服务响应
	 */
	/*成功*/
	SUCCESS( 0, "success" ),

	/*失败*/
	FAIL( 102070400, "fail" ),

	/*请求超时*/
	REQUEST_TIMEOUT( 102070401, "request timeout" ),

	/*未知异常*/
	UNKNOWN_EXCEPTION_CODE( 102070402, "unknown exception code" ),

	/*渠道服务发送失败*/
	CHANNEL_SERVICE_DELIVERY_FAILURE( 102070403, "channel service delivery failure" ),

	/*无权限用户*/
	UNAUTHORIZED_USERS( 102070404, "unauthorized users" ),

	/**
	 * 0500-0699：请求参数验证
	 */

	/*短信联系人不能为空*/
	CONTACT_NUST_BE_NOT_BLANK( 102070501, "contact must be not blank" ),

	/*短信模板不能为空*/
	TEMPLATE_MUST_BE_NOT_BLANK( 102070502, "template must be not blank" ),

	/*区域不能为空*/
	LOCALE_MUST_BE_NOT_BLANK( 102070503, "locale must be not blank" ),

	/*不支持该国家*/
	THIS_REGION_IS_NOT_SUPPORTED( 102070504, "this region is not supported" ),

	/*ip不能为空*/
	IP_MUST_BE_NOT_BLANK( 102070505, "ip must be not blank" );


	private int codeNo;

	private String codeName;

	private static Map<Integer, String> map = new HashMap<Integer, String>();

	private static List<ExceptionCode> list = new ArrayList<ExceptionCode>();

	static {
		for (ExceptionCode status : ExceptionCode.values()) {
			map.put(status.getCodeNo(), status.getCodeName());
		}
		list.addAll(Arrays.asList(ExceptionCode.values()));
	}

	public int getCodeNo() {
		return codeNo;
	}

	public void setCodeNo(int codeNo) {
		this.codeNo = codeNo;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	private ExceptionCode(int codeNo, String codeName) {
		this.codeNo = codeNo;
		this.codeName = codeName;
	}


	public static Map<Integer, String> getMap() {
		return map;
	}

	public static List<ExceptionCode> getList() {
		return list;
	}


	public static ExceptionCode getCategory(int codeNo) {
		for (ExceptionCode status : list) {
			if (status.getCodeNo() == codeNo) {
				return status;
			}
		}
		return null;
	}


	public static String getName(int codeNo) {
		for (ExceptionCode status : list) {
			if (status.getCodeNo() == codeNo) {
				return status.getCodeName();
			}
		}
		return null;
	}
}
