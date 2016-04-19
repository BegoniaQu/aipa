package com.aipa.svc.common.util;

import com.qy.data.common.util.StringUtil;

public class TokenUtil_RE
{
    public static final int TOKEN_FIELD_COUNT = 3;
    public static boolean isTokenValidated(String token)
    {
        return true;
    }
    
    public static String decryptToken(String encryptedToken) throws Exception
    {
        // hex转换
        byte[] transformed = CoderUtil.hexStringToBytes(encryptedToken);
        // 公钥解密
        return new String(CoderUtil.decrypt(CoderUtil.getPublicKey(), transformed));
    }

    public static long getUidFromToken(String encryptedToken)
    {
        long result = -1;
        if (encryptedToken == null)
        {
            return result;
        }
        
        try
        {
            String origData = decryptToken(encryptedToken);
            String[] fields = origData.split("&");
            if (fields.length == TOKEN_FIELD_COUNT && StringUtil.getLong(fields[1]) > System.currentTimeMillis())
            {
                result = Long.valueOf(fields[0]);
            }
        }
        catch(Exception e)
        {
        }
        
        return result;
    }

    public static boolean isTokenExpired(long expiredTime)
    {
        return (expiredTime < System.currentTimeMillis());
    }
}
