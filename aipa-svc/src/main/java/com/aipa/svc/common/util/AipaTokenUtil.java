package com.aipa.svc.common.util;


public class AipaTokenUtil {

	private static final long mod = 10000000000l;
	private static final long second = 1000l;
	private static final String keyStr = "aipa20160425ququ";
	public static final long expired = 3600000; //one hour
	
	
	
	public static String getToken(long uid){
		long time = System.currentTimeMillis();
		long later = time/second;
		long f = later%mod;
		String v = uid+"&"+f;
		try {
			byte [] b = AESUtil.encrypt(v.getBytes(), keyStr.getBytes());
			return HexUtil.toHexString(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String verifyToken(String token){
		try {
			byte [] encryptedV = HexUtil.toByteArray(token);
			byte[] b = AESUtil.decrypt(encryptedV, keyStr.getBytes());
			String v = new String(b);
			return v.split("&")[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String token = getToken(123);
		System.out.println(token);
		System.out.println(verifyToken(token));
	}
	
	
}
