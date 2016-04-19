package com.qy.data.common.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.qy.data.common.util.StringUtil;

public final class LongConvert implements LangConvert<Long>{

	private static LongConvert convert = new LongConvert();
	
	private LongConvert(){}
	
	public static LongConvert getConvert(){
		return convert;
	}

	@Override
	public Long getValue(String orgin) {
		return getValue(orgin, null);
	}
	
	@Override
	public Long getValue(String orgin, Long defValue) {
		if( StringUtil.isEmpty(orgin) ) {
			return defValue;
		}
		
		return Long.valueOf(orgin);
	}

	@Override
	public Long getValue(Object orgin) {
		if( orgin == null ) {
			return null;
		}
		
		return getValue(String.valueOf(orgin));
	}

	@Override
	public Set<Long> getValues(Set<String> orgins) {
		if(orgins != null){
			Set<Long> set = new HashSet<Long>(orgins.size());
			for(String org : orgins){
				set.add(getValue(org));
			}
			return set;
		}
		return Collections.emptySet();
	}
	
	@Override
	public String toValue(Long origin) {
		if(null == origin)
			return null;
		
		return origin.toString();
	}

	@Override
	public Set<String> toValues(Set<Long> orgins) {
		if(orgins != null && !orgins.isEmpty()){
			Set<String> set = new HashSet<String>(orgins.size());
			for(Long org : orgins){
				set.add(toValue(org));
			}
			return set;
		}
		return Collections.emptySet();
	}
}
