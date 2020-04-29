package org.apdoer.channel.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.channel.server.model.po.MsgRecordPo;
import org.apdoer.common.service.common.BaseMapper;

public interface MsgRecordMapper extends BaseMapper<MsgRecordPo> {

	/**
	 * 插入到db
	 */
	 int insert(MsgRecordPo record);

	/**
	 * 根据id查询msg
	 */
	 MsgRecordPo queryMsgRecordById(Long id);

	/**
	 * 未发送-->已发送
	 */
	 Integer update2Sended(@Param("id") Long id, @Param("responseText") String responseText, @Param("resId") String resId);

	/**
	 * 未发送-->已发送
	 */
	 Integer update2Sended4Ali(@Param("id") Long id, @Param("responseText") String responseText, @Param("reqId") String reqId,
                                     @Param("resId") String resId);

	/**
	 * 已发送-->发送成功
	 */
	 Integer update2Success(@Param("id") Long id, @Param("responseText") String responseText);

	/**
	 * 已发送-->发送失败
	 */
	 Integer update2Failure(@Param("id") Long id, @Param("responseText") String responseText);

	/**
	 * 已发送-->发送异常
	 */
	 Integer update2Exception(@Param("id") Long id, @Param("responseText") String responseText);

}
