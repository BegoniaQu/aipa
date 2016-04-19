package com.qy.data.common.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * @author qy
 */
public interface GenericMapInfo extends Serializable {
	public Map<String, Object> getAttributes();
	public void setAttributes(Map<String, Object> map);
}
