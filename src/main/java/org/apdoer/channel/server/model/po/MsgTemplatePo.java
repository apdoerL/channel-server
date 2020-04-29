package org.apdoer.channel.server.model.po;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author apdoer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MsgTemplatePo implements Serializable {

	private static final long serialVersionUID = -4808789317722284511L;
	private Long				id;
	/* 业务类型 */
	private String              bussinessType;
	/* 模板代码 */
	private String				templateCode;
	/* 语言_国家,zh_CN */
	private String				locale;
	/* 标题 */
	private String				subject;
	/* 模板内容 */
	private String				content;
	/* 模板代码 */
	private String				remark;
	/* 状态,0启用,1弃用 */
	private Boolean				status;
	/* 备注 */
	private Date				createTime;
	/* 创建时间 */
	private Date				updateTime;

	
}
