/**
 * 
 */
package com.qy.data.common.annotation.handler;

import java.util.Set;

import com.qy.data.common.annotation.holder.BeanClassAnnotationHolder;
import com.qy.data.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;

import com.qy.data.common.annotation.bean.BeanClass;

/**
 * @author yonka
 * @Date 2015-5-12
 */
public class BeanClassAnnotationScanner {
	
	private BeanClassAnnotationHolder beanClassAnnotationHolder;
	
	private String rawPackageName;
	
	public BeanClassAnnotationScanner(){}
	
	public void init(){
		scanClassPathDomainClasses(rawPackageName);
	}
	
	/**
	 * 允许\n分割的多个包路径
	 * @param packageName
	 */
	public void scanClassPathDomainClasses(String packageName){
		if(StringUtil.isEmpty(packageName)) return;
		String[] packageNames = StringUtils.split(packageName, "\n");
		if(packageNames == null || packageNames.length == 0) return;
		for (String subPackageName : packageNames) {
			try{
				Reflections reflections = new Reflections(StringUtils.strip(subPackageName));
				Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(BeanClass.class);

				for (Class<?> beanClass : annotated) {
					if(beanClassAnnotationHolder != null){
						beanClassAnnotationHolder.ensureBeanClassInfo(beanClass);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public BeanClassAnnotationHolder getBeanClassAnnotationHolder() {
		return beanClassAnnotationHolder;
	}

	public void setBeanClassAnnotationHolder(BeanClassAnnotationHolder beanClassAnnotationHolder) {
		this.beanClassAnnotationHolder = beanClassAnnotationHolder;
	}

	public String getRawPackageName() {
		return rawPackageName;
	}

	public void setRawPackageName(String rawPackageName) {
		this.rawPackageName = rawPackageName;
	}
}
