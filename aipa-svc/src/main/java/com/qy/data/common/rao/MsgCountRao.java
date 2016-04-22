package com.qy.data.common.rao;

/**
 * 对某类消息的处理
 * @author qy
 *
 */
public interface MsgCountRao {
	public void incre(String key);

	public long get(String key);
	
	public void del(String key);
}
