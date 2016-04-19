
package com.qy.data.common.annotation.holder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.qy.data.common.data.container.impl.IterableObjectsJoinerAdapter;
import com.qy.data.common.annotation.bean.BeanClass;
import com.qy.data.common.annotation.bean.BeanField;

/**
 * 
 * @author qy
 *
 */
public class BeanClassAnnotationHolder {

	private Map<Class<?>, BeanClassInfo> beanClassInfoMap = new HashMap<Class<?>, BeanClassInfo>();
	
	public void addFieldName(Class<?> beanClass, String fieldName){
		if(beanClass == null || fieldName == null) return;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		beanClassInfo.addFieldName(fieldName);
	}
	
	public Set<String> getFieldNames(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		return beanClassInfo.getFieldNames();
	}
	
	public void addExternalFieldName(Class<?> beanClass, String fieldName){
		if(beanClass == null || fieldName == null) return;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		beanClassInfo.addExternalFieldName(fieldName);
	}
	
	public Set<String> getExternalFieldNames(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		return beanClassInfo.getExternalFieldNames();
	}
	
	public void addOptionalFieldName(Class<?> beanClass, String fieldName){
		if(beanClass == null || fieldName == null) return;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		beanClassInfo.addOptionalFieldName(fieldName);
	}
	
	public Set<String> getOptionalFieldNames(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		return beanClassInfo.getOptionalFieldNames();
	}
	
	public void addUserSpecificFieldName(Class<?> beanClass, String fieldName){
		if(beanClass == null || fieldName == null) return;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		beanClassInfo.addUserSpecificFieldName(fieldName);
	}
	
	public Set<String> getUserSpecificFieldNames(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		return beanClassInfo.getUserSpecificFieldNames();
	}	
	
	public void addDerivedFieldName(Class<?> beanClass, String fieldName){
		if(beanClass == null || fieldName == null) return;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		beanClassInfo.addDerivedFieldName(fieldName);
	}
	
	public Set<String> Derived(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = ensureBeanClassInfo(beanClass);
		return beanClassInfo.getDerivedFieldNames();
	}		
	
	/**
	 * 支持lazy解析
	 * @param beanClass
	 * @return
	 */
	public BeanClassInfo ensureBeanClassInfo(Class<?> beanClass){
		if(beanClass == null) return null;
		BeanClassInfo beanClassInfo = beanClassInfoMap.get(beanClass);
		if(beanClassInfo == null){
			beanClassInfo = new BeanClassInfo(); 
			beanClassInfoMap.put(beanClass, beanClassInfo);
			parseBeanClass2Info(beanClass, beanClassInfo);
		}
		return beanClassInfo;
	}
	
	public void saveBeanFieldInfo2BeanClassInfo(BeanField beanField, BeanClassInfo beanClassInfo){
		if(beanField == null || beanClassInfo == null) return;
		
		String fieldName = beanField.name();
		beanClassInfo.addFieldName(fieldName);
		if(beanField.isOptional()) beanClassInfo.addOptionalFieldName(fieldName);
		if(beanField.isExternal()) beanClassInfo.addExternalFieldName(fieldName);
		if(beanField.isUserSpecific()) beanClassInfo.addUserSpecificFieldName(fieldName);
		if(beanField.isDerived()) beanClassInfo.addDerivedFieldName(fieldName);
	}
	
	public void parseBeanClass2Info(Class<?> beanClass, BeanClassInfo beanClassInfo){
		if(beanClass == null || beanClassInfo == null) return;
		Field[] fields = beanClass.getFields();
		Field[] declaredFields = beanClass.getDeclaredFields();
		for (Field field : new IterableObjectsJoinerAdapter<Field>(Arrays.asList(fields), Arrays.asList(declaredFields))) {
			BeanField beanField = field.getAnnotation(BeanField.class);
			if(beanField != null){
				saveBeanFieldInfo2BeanClassInfo(beanField, beanClassInfo);
			}
		}
		
		Class<?> superClass = beanClass.getSuperclass();
		if(superClass.getAnnotation(BeanClass.class) != null){
			parseBeanClass2Info(superClass, beanClassInfo);
		}
	}
	
	static class BeanClassInfo{
		public Set<String> fieldNames = new HashSet<String>();
		public Set<String> externalFieldNames = new HashSet<String>();
		public Set<String> optionalFieldNames = new HashSet<String>();
		public Set<String> userSpecificFieldNames = new HashSet<String>();
		public Set<String> derivedFieldNames = new HashSet<String>();
		
		public void addFieldName(String fieldName){
			fieldNames.add(fieldName);
		}
		public Set<String> getFieldNames(){
			return fieldNames;
		}
		
		public void addExternalFieldName(String fieldName){
			externalFieldNames.add(fieldName);
		}
		public Set<String> getExternalFieldNames(){
			return externalFieldNames;
		}
		
		public void addOptionalFieldName(String fieldName){
			optionalFieldNames.add(fieldName);
		}
		public Set<String> getOptionalFieldNames(){
			return optionalFieldNames;
		}
		
		public void addUserSpecificFieldName(String fieldName){
			userSpecificFieldNames.add(fieldName);
		}
		public Set<String> getUserSpecificFieldNames(){
			return userSpecificFieldNames;
		}
		
		public void addDerivedFieldName(String fieldName){
			derivedFieldNames.add(fieldName);
		}
		public Set<String> getDerivedFieldNames(){
			return derivedFieldNames;
		}
	}
}
