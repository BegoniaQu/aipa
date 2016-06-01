package com.aipa.svc.common.config;

import com.aipa.svc.common.enume.ImgBusiType;

public class ThirdConfig {

	public static final String domain = "";
	public static final String QINIU_BUCKET= "develop";	//测试环境
	public static final String QINIU_NOTE_IMG_DIR = ImgBusiType.notepic.name();
	public static final String QINIU_HEAD_IMG_DIR = ImgBusiType.headpic.name();
	public static final long  QINIU_TOKEN_EXPIRED = 1*60; //1分钟
	public static String QINIU_AK = "";
	public static String QINIU_SK = "";
}
