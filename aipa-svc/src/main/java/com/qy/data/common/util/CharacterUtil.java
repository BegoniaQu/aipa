package com.qy.data.common.util;

/**
 * 
 * @author qy
 *
 */
public class CharacterUtil {
	public static boolean isValidCodePoint(int codePoint, boolean validateSurrogatePair){
		boolean originValidateResult = Character.isValidCodePoint(codePoint);
		if(!originValidateResult || !validateSurrogatePair) return originValidateResult;
		else {
			char[] chars = Character.toChars(codePoint);
			if(chars.length == 1) return !isSurrogate(chars[0]);
			// 0为high， 1为low
			else return Character.isSurrogatePair(chars[0], chars[1]);
		}
	}
	
	public static boolean isSurrogate(char c){
		return Character.isHighSurrogate(c) || Character.isLowSurrogate(c);
	}
	
	/**
	 * 
	 * @param codePoint
	 * @return null means non-bmp
	 */
	public static Boolean isSurrogate(int codePoint){
		if(!Character.isBmpCodePoint(codePoint)) return null;
		// bmp可以转为char
		return isSurrogate((char)codePoint);
	}
	
	/**
	 * 
	 * @param codePoint
	 * @return codePoint小于0时为非法，返回-1
	 */
	public static int lenCodePoint(int codePoint){
		if(codePoint < 0) return -1;
		else if(!Character.isValidCodePoint(codePoint)) return 2;
		else return Character.isBmpCodePoint(codePoint) ? 1 : 2;
	}
}
