package com.qy.data.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author qy
 *
 */
public class StringUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(StringUtil.class);

	private static final int[] md5Accii = new int[128];
	static {
		md5Accii['0'] = 1;
		md5Accii['1'] = 1;
		md5Accii['2'] = 1;
		md5Accii['3'] = 1;
		md5Accii['4'] = 1;
		md5Accii['5'] = 1;
		md5Accii['6'] = 1;
		md5Accii['7'] = 1;
		md5Accii['8'] = 1;
		md5Accii['9'] = 1;
		md5Accii['a'] = 1;
		md5Accii['b'] = 1;
		md5Accii['c'] = 1;
		md5Accii['d'] = 1;
		md5Accii['e'] = 1;
		md5Accii['f'] = 1;
		md5Accii['A'] = 1;
		md5Accii['B'] = 1;
		md5Accii['C'] = 1;
		md5Accii['D'] = 1;
		md5Accii['E'] = 1;
		md5Accii['F'] = 1;
	}
	
	public static final int STR_FORMAT_ORIGIN = 0;
	public static final int STR_FORMAT_LOWER_CASE = 1;
	public static final int STR_FORMAT_UPPER_CASE = 2;
	public static final int STR_FORMAT_CAPTITALIZED = 3;
	public static final int STR_FORMAT_CAMEL = 4;
	
	/**
	 * 
	 * @param srcStr
	 * @param attrFormat 需要符合这里定义的 ATTR_FORMAT_*，否则返回null...
	 * @return
	 */
	public static String getStringOfFormat(String srcStr, int attrFormat){
		if(StringUtil.isEmpty(srcStr) || STR_FORMAT_ORIGIN == attrFormat) return srcStr;
		else if(STR_FORMAT_CAMEL == attrFormat) return StringUtil.camelizeString(srcStr);
		else if(STR_FORMAT_CAPTITALIZED == attrFormat) return StringUtils.capitalize(srcStr);
		else if(STR_FORMAT_LOWER_CASE == attrFormat) return srcStr.toLowerCase();
		else if(STR_FORMAT_UPPER_CASE == attrFormat) return srcStr.toUpperCase();
		else return null;
	}

	/**
	 * 从字符串描述的数组中剔除固定的内容
	 * 
	 * @param src
	 *            字串串表示的数组
	 * @param rStr
	 *            待剔除的内容
	 * @param spliter
	 *            字符串数组分割字符
	 * @return
	 */
	public static String removeItem(String src, String rStr, String spliter) {
		StringBuilder sb = new StringBuilder();
		String[] temp = src.split(spliter);
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < temp.length; i++) {
			if (!temp[i].equalsIgnoreCase(rStr)
					&& !ar.contains(Integer.parseInt(temp[i]))) {
				sb.append(temp[i]);
				sb.append(spliter);
				ar.add(Integer.parseInt(temp[i]));
			}
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - spliter.length());
		} else {
			return null;
		}
	}
	public  static   <T extends Object>  String convertObjectList2String(List<T> membetrs,String sep){
		if (null==membetrs){
			return  null;
		}
		if (membetrs.size()==0){
			return  "";
		}
		StringBuffer sb=new StringBuffer();
		for (T object:membetrs) sb.append(object.toString()).append(sep);
		String result=sb.toString();
		result=result.substring(0, result.length() - sep.length());
		return  result;
	}

	public static String upperTrimInital(String string) {
		return StringUtils.capitalize(string.trim());
	}

	/**
	 * 获得字符串的MD5
	 * 
	 * @param strP
	 *            字符串
	 * @return 字符串MD5
	 */
	public static String toMD5(String strP) {
		byte[] source = null;
		try {
			source = strP.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (Exception e) {
			logger.error("toMD5 error", e);
		}
		return s;
	}

	public static String[] convertFromSet(List<Long> list) {
		if (null == list)
			return new String[] {};
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = String.valueOf(list.get(i));
		}
		return array;
	}

	public static boolean checkMd5Format(String md5Str) {
		if (md5Str == null || md5Str.length() != 32) {
			return false;
		}

		for (int i = 0; i < md5Str.length(); i++) {
			int index = md5Str.charAt(i);
			if (index >= md5Accii.length || index < 0 || md5Accii[index] != 1) {
				return false;
			}
		}

		return true;
	}

	public static boolean isEmpty(String value) {
		if (null == value) {
			return true;
		}
		for (int length = value.length(), i = 0; i < length; i++) {
			if (' ' != value.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public static long getBytesConfigByString(String str) {
		long value = 0;
		str = str.trim().toLowerCase();
		if (!"".equals(str)) {
			if ('m' == str.charAt(str.length() - 1)) {
				value = (long) (Double.parseDouble(str.substring(0,
						str.length() - 1)) * 1024 * 1024);
			} else if ('g' == str.charAt(str.length() - 1)) {
				value = (long) (Double.parseDouble(str.substring(0,
						str.length() - 1)) * 1024 * 1024 * 1024);
			} else if ('t' == str.charAt(str.length() - 1)) {
				value = (long) (Double.parseDouble(str.substring(0,
						str.length() - 1)) * 1024 * 1024 * 1024 * 1024);
			} else if ('k' == str.charAt(str.length() - 1)) {
				value = (long) (Double.parseDouble(str.substring(0,
						str.length() - 1)) * 1024);
			} else {
				value = (long) Double.parseDouble(str);
			}
		}
		return value;
	}

	public static boolean isAllChar(String src, char c) {
		if (null == src || src.length() == 0) {
			return false;
		}

		int len = src.length();

		for (int i = 0; i < len; i++) {
			if (src.charAt(i) != c) {
				return false;
			}
		}
		return true;
	}

	public static long getLong(String strTemp) {
		strTemp = notNull(strTemp);
		if (strTemp.equals("")) {
			return 0L;
		}
		try {
			return (long) Math.floor(Double.parseDouble(strTemp));
		} catch (Exception e) {
			return 0L;
		}
	}

	public static String notNull(String strTemp) {
		if (strTemp == null) {
			return new String("");
		} else {
			return strTemp;
		}
	}

	public static String notNullTrim(String strTemp) {
		if (strTemp == null) {
			return new String("");
		} else {
			return strTemp.trim();
		}
	}

	public static String wrapXmlContent(String content) {
		StringBuilder appender = new StringBuilder("");
		if ((content != null) && (!content.trim().isEmpty())) {
			appender = new StringBuilder(content.length());
			for (int i = 0; i < content.length(); i++) {
				char ch = content.charAt(i);
				if ((ch == '\t') || (ch == '\n') || (ch == '\r')
						|| ((ch >= ' ') && (ch <= 55295))
						|| ((ch >= 57344) && (ch <= 65533))
						|| ((ch >= 65536) && (ch <= 1114111))) {

					appender.append(ch);
				}
			}
		}
		return appender.toString();
	}

	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(url + " decode error.", e);
		}
	}

	public static String decodeUrl(String str) {
		if (str.indexOf("%") >= 0) {
			try {
				return URLDecoder.decode(str, "UTF-8");
			} catch (Exception e) {
			}
		}

		return str;
	}

	public static String filterMoreThanOneEndOfLineCharacter(String s){
		if(isEmpty(s)) return s;
		
		int indexOfNL = s.indexOf("\n");
		int indexOfCT = s.indexOf("\r");
		int indexOfNLCT = s.indexOf("\n\r");
		int lastIndex = -1;
		String endOfLine = null;
		if(indexOfNLCT >= 0){
			endOfLine = "\n\r";
			lastIndex = indexOfNLCT;
		} else if(indexOfNL >= 0){
			endOfLine = "\n";
			lastIndex = indexOfNL;
		} else if(indexOfCT >= 0){
			endOfLine = "\r";
			lastIndex = indexOfCT;
		}
		int endOfLineLength = endOfLine == null ? 0 : endOfLine.length();
		
		if(endOfLine != null){
			char[] cArr = new char[s.length()];
			char[] origCharArr = s.toCharArray();
			int cnt = 0;
			
			if(lastIndex >= 0) {
				cnt += (lastIndex + endOfLineLength - 0);
				System.arraycopy(origCharArr, 0, cArr, 0, cnt);
			}
			while(lastIndex >= 0){
				int curStartIndex = lastIndex + endOfLineLength;
				int newIndex = s.indexOf(endOfLine, curStartIndex);
				if(newIndex > curStartIndex){
					int newStartIndex = newIndex + endOfLineLength;
					int newCnt = newStartIndex - curStartIndex;
					System.arraycopy(origCharArr, curStartIndex, cArr, cnt, newCnt);
					cnt += newCnt;  // 包括新的这个换行符也要复制进去
				}
				lastIndex = newIndex;
			}
			s = new String(cArr, 0, cnt);
		}
		
		return s;
	}
	
	public static String replaceAllLineBreaks(String s, String replacement){
		if(isEmpty(s)) return s;
		
		int indexOfNL = s.indexOf("\n");
		int indexOfCT = s.indexOf("\r");
		int indexOfNLCT = s.indexOf("\n\r");
		String endOfLine = null;
		if(indexOfNLCT >= 0){
			endOfLine = "\n\r";
		} else if(indexOfNL >= 0){
			endOfLine = "\n";
		} else if(indexOfCT >= 0){
			endOfLine = "\r";
		}
		if(endOfLine == null) return s;
		return s.replaceAll(endOfLine, replacement);
	}
	
	public static String truncateString(String srcStr, int maxLength, String suffix, boolean ensureFinalLength){
		//XXX 考虑换成 codepointSubString
		if(srcStr == null || srcStr.length() <= maxLength) return srcStr;
		else if(maxLength <= 0) return "";
		else{
			if(ensureFinalLength){
				int newMaxLength = maxLength - (suffix == null ? 0 : suffix.length());
				if(newMaxLength == 0) return suffix;
				else if(newMaxLength < 0) return suffix.substring(0, maxLength);
				else{
					return srcStr.substring(0, newMaxLength) + (suffix == null ? "" : suffix);
				}
			}else{
				return srcStr.substring(0, maxLength) + (suffix == null ? "" : suffix);
			}
		}
	}
	
	public static String camelizeString(String srcStr){
		if(StringUtil.isEmpty(srcStr)) return srcStr;
		Character c = srcStr.charAt(0);
		if(Character.isUpperCase(c)){
			StringBuilder sb = new StringBuilder();
			sb.append(Character.toLowerCase(c));
			sb.append(srcStr.substring(1));
			return new String(sb);
		}else{
			return srcStr;
		}
	}

	
	public static String removeAllEmoji(String srcStr){
		if(isEmpty(srcStr)) return srcStr;
		else return srcStr.replaceAll("\\p{So}+", "");
	}
	
	public static String codepointSubString(String srcStr, int startIndex, int endIndex, boolean stripInvalidCodePoint){
		if(isEmpty(srcStr)) return srcStr;
		int len = srcStr.length();
		if(startIndex < 0) startIndex = 0;
		if(endIndex > len - 1) endIndex = len;
		if(startIndex >= endIndex) return "";

		int processCharCnt = 0;
		
		if(stripInvalidCodePoint){
			
			char[] resCharArr = null;
			int resCharArrDstIndex = -1;
			int validCodePointCharCnt = 0;
			int validCodePointCnt = 0;
//			int curCodePointIndex = 0;
			while(processCharCnt < len){
				int codePoint = getNextCodePoint(srcStr, processCharCnt);
//				int codePoint = srcStr.codePointAt(curCodePointIndex++);
				int codePointLen = 0;
				boolean isValidCodePoint = CharacterUtil.isValidCodePoint(codePoint, true);
				if(isValidCodePoint){
					validCodePointCnt++;
					if(validCodePointCnt > endIndex) break;
					else if(validCodePointCnt > startIndex && resCharArrDstIndex >= 0){
						codePointLen = Character.toChars(codePoint, resCharArr, resCharArrDstIndex);
						resCharArrDstIndex += codePointLen;
					}else codePointLen = CharacterUtil.lenCodePoint(codePoint);
					validCodePointCharCnt += codePointLen;
				}else{
					// 因为codepoint来自 String.codePointAt，所以不可能<0，那么只能是太大... len也是为2
					codePointLen = CharacterUtil.lenCodePoint(codePoint);
					if(resCharArrDstIndex < 0){
						resCharArrDstIndex = 0;
						resCharArr = new char[len];
						if(validCodePointCharCnt > 0) {
							copyStr2CharArr(srcStr, 0, resCharArr, resCharArrDstIndex, validCodePointCharCnt, false);
							resCharArrDstIndex += validCodePointCharCnt;
						}
					}
				}
				
				processCharCnt += codePointLen;
			}
			if(validCodePointCharCnt == 0) return "";
			else if(resCharArrDstIndex < 0) return srcStr.substring(0, validCodePointCharCnt);
			else return new String(resCharArr, 0, resCharArrDstIndex);
		}else{
			int codePointCnt = srcStr.codePointCount(0, len);
			endIndex = codePointCnt < endIndex ? codePointCnt : endIndex;
			return srcStr.substring(srcStr.offsetByCodePoints(0, startIndex), srcStr.offsetByCodePoints(0, endIndex));
		}
	}
	
	public static String codepointSubString(String srcStr, int startIndex, boolean stripInvalidCodePoint){
		if(isEmpty(srcStr)) return srcStr;
		if(startIndex < 0) startIndex = 0;
		return codepointSubString(srcStr, startIndex, srcStr.length(), stripInvalidCodePoint);
	}
	
	public static void copyStr2CharArr(String srcStr, int srcStartIndex, char[] dstCharArr, int dstStartIndex, int offset, boolean enforce){
		if(!enforce){
			if(srcStr == null || dstCharArr == null || srcStartIndex < 0 || dstStartIndex < 0) return;
			int srcLen = srcStr.length();
			int dstLen = dstCharArr.length;
			if(srcStartIndex > srcLen - 1 || dstStartIndex > dstLen - 1) return;
			offset = Math.min(Math.min(srcLen - srcStartIndex, dstLen - dstStartIndex), offset);
		}

		for(int i=0; i<offset; i++){
			dstCharArr[dstStartIndex + i] = srcStr.charAt(srcStartIndex + i);
		}
	}
	
	/**
	 * 注意： 给定的startIndex本身可能已经截断了...，这里是不关心的...
	 * @param srcStr
	 * @param startIndex
	 * @return -1表示该str已经没有更多码点了
	 */
	public static int getNextCodePoint(String srcStr, int startIndex){
		if(srcStr == null) return -1;
		int len = srcStr.length();
		if(startIndex >= len) return -1;
		else if(startIndex == len -1) return (int)srcStr.charAt(startIndex);
		else{
			char c = srcStr.charAt(startIndex++);
			if(Character.isHighSurrogate(c)){
				char c1 = srcStr.charAt(startIndex);
				if(Character.isLowSurrogate(c1)){
					return Character.toCodePoint(c, c1);
				}
			}
			return (int)c;
		}
	}
	
	/**
	 * 只用于被从0截取的str的clean操作 - 该情况下因截取而产生的invalid codepoint只会在最末尾
	 * @param srcStr
	 * @return
	 */
	public static String rstripMalformedUnicodeString(String srcStr){
		if(isEmpty(srcStr)) return srcStr;
		int len = srcStr.length();
		int curIndex = len;
		while(curIndex > 0){
			int codePoint = srcStr.codePointBefore(curIndex);
			if(CharacterUtil.isValidCodePoint(codePoint, true)) break;
			else curIndex -= CharacterUtil.lenCodePoint(codePoint);  
			// 有可能是非法（过大）的codepoint，虽然这种codepoint并非因为截取产生，但... 
		}
		if(curIndex == len) return srcStr;
		else return srcStr.substring(0, curIndex);
	}
	
	public static String removeAllMalformedUnicodeString(String srcStr){
		if(isEmpty(srcStr)) return srcStr;
		return codepointSubString(srcStr, 0, true);
	}
	
	public void testCodepointSubString(){
		int[] codePoints1 = {0xD83D, 0xDC4F, 0xD83D};
		String s1 = new String(codePoints1, 0, codePoints1.length);
		// 改为assert
		System.out.println(codepointSubString(s1, 0, 0, true));
		System.out.println(codepointSubString(s1, 0, 1, true));
		System.out.println(codepointSubString(s1, 0, 2, true));
		System.out.println(codepointSubString(s1, 0, 0, false));
		System.out.println(codepointSubString(s1, 0, 1, false));
		System.out.println(codepointSubString(s1, 0, 2, false));
		
		int[] codePoints2 = {0xDC4F, 0xD83D};
		String s2 = new String(codePoints2, 0, codePoints2.length);
		System.out.println(codepointSubString(s2, 0, 0, true));
		System.out.println(codepointSubString(s2, 0, 1, true));
		System.out.println(codepointSubString(s2, 0, 2, true));
		System.out.println(codepointSubString(s2, 0, 0, false));
		System.out.println(codepointSubString(s2, 0, 1, false));
		System.out.println(codepointSubString(s2, 0, 2, false));
	}
	
	public void testRstripMalformedUnicodeString(){
		int[] codePoints1 = {0xD83D, 0xDC4F, 0xD83D};
		String s1 = new String(codePoints1, 0, codePoints1.length);
		// 改为assert
		System.out.println(rstripMalformedUnicodeString(s1));
		
		int[] codePoints2 = {0xDC4F, 0xD83D};
		String s2 = new String(codePoints2, 0, codePoints2.length);
		System.out.println(rstripMalformedUnicodeString(s2));
		
		int[] codePoints3 = {0xD83D, 0xDC4F};
		String s3 = new String(codePoints3, 0, codePoints3.length);
		System.out.println(rstripMalformedUnicodeString(s3));
	}
	
	public void testRemoveAllMalformedUnicodeString(){
		System.out.println("test RemoveAllMalformedUnicodeString");
		int[] codePoints1 = {0xD83D, 0xDC4F, 0xD83D};
		String s1 = new String(codePoints1, 0, codePoints1.length);
		// 改为assert
		System.out.println(removeAllMalformedUnicodeString(s1));
	}
}
