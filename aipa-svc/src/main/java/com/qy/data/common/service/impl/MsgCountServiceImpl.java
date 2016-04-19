package com.qy.data.common.service.impl;

import com.qy.data.common.rao.MsgCountRao;
import com.qy.data.common.service.MsgCountService;

/**
 * @author qy
 *
 */
public class MsgCountServiceImpl implements MsgCountService {

	private MsgCountRao msgCountRao;
	
	@Override
	public void incre(String key) {
		msgCountRao.incre(key);
	}

	@Override
	public long get(String key) {
		return msgCountRao.get(key);
	}

	@Override
	public void del(String key) {
		msgCountRao.del(key);
	}

	public void setMsgCountRao(MsgCountRao msgCountRao) {
		this.msgCountRao = msgCountRao;
	}
}
