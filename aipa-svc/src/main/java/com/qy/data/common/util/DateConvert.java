package com.qy.data.common.util;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 时间转换为Long，和Long转换为时间
 * @author qy
 * @date   2016
 */
public final class DateConvert implements LangConvert<Date>{

	private static DateConvert convert = new DateConvert();
	
	private DateConvert(){}
	
	public static DateConvert getConvert(){
		return convert;
	}

	@Override
	public Date getValue(String orgin) {
		return getValue(orgin, null);
	}
	
	@Override
	public Date getValue(String orgin, Date defValue) {
		Long longValue = LongConvert.getConvert().getValue(orgin);
		return longValue == null ? defValue : new Date(longValue);
	}

	@Override
	public Date getValue(Object orgin) {
		if( orgin == null ) {
			return null;
		}
		
		return getValue(String.valueOf(orgin));
	}
	
	@Override
	public Set<Date> getValues(Set<String> orgins) {
		if(orgins != null){
			Set<Date> set = new HashSet<Date>(orgins.size());
			for(String org : orgins){
				set.add(getValue(org));
			}
			return set;
		}
		return Collections.emptySet();
	}
	
	@Override
	public String toValue(Date origin) {
		if(null == origin)
			return null;

		return String.valueOf(origin.getTime());
	}

	@Override
	public Set<String> toValues(Set<Date> orgins) {
		if(orgins != null && !orgins.isEmpty()){
			Set<String> set = new HashSet<String>(orgins.size());
			for(Date org : orgins){
				set.add(toValue(org));
			}
			return set;
		}
		return Collections.emptySet();
	}
}
