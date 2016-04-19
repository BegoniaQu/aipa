package com.aipa.svc.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CoderUtil
{
    private static Logger logger = Logger.getLogger(CoderUtil.class);
    public static final String KEY_SHA = "SHA";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    @SuppressWarnings("unused")
	private static final String Default_AESKey = "1k3f5x7v90AeQPCZ";
    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;

    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    @SuppressWarnings("unused")
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private final static Map<Character, String> URLEncodeMap = new HashMap<Character, String>();
    private final static Map<String, Character> URLDecodeMap = new HashMap<String, Character>();

    @SuppressWarnings("unused")
	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnKO1Xc/uS57Wkd3T2UHEJ5/06" + "\r" + "IBHUONZ5vYdpHlYrLOQMApma36vQkeucgfsClnxS7fmj1X701/lPKSx0HutgxsFk" + "\r" + "6ddg7ZXMaGH3D5ZF+srNnJX38NoyRCTvAoqwYE1WR3YuskRSoI5XbgwA3PaiXbxi" + "\r" + "YRg7W8Y3umOWOsSgOQIDAQAB" + "\r";

    @SuppressWarnings("unused")
	private static final String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOco7Vdz+5LntaR3" + "\r" + "dPZQcQnn/TogEdQ41nm9h2keViss5AwCmZrfq9CR65yB+wKWfFLt+aPVfvTX+U8p" + "\r" + "LHQe62DGwWTp12DtlcxoYfcPlkX6ys2clffw2jJEJO8CirBgTVZHdi6yRFKgjldu" + "\r" + "DADc9qJdvGJhGDtbxje6Y5Y6xKA5AgMBAAECgYBBr7iZ9ERVv3A3Vxauc7dbaEfl" + "\r" + "/3zxgGE54Oicr1kvFaeL3iVGBC8vqnd4Vts8JgUYD7Wzn5RfAQxAOWP/b1XU9OGB" + "\r" + "SlSeX+DO/H03Y1TPac+iZ2ltqPTF1lfRgtlTxBsWYDy72F8Q9CEy2P+RRFUUf0Ab" + "\r" + "gVZDfaKhq49jexqo3QJBAP5hS9nKTRiVB+T2jy3h3mzqm8CTXYJkzF8AKx0kteus" + "\r" + "+PltlMpO4dgpM7lt7R7DdI0HLWYG06LZqIfGsRMDMHMCQQDoocarNVvNNMy+Tnih" + "\r" + "jB4Aj8nzDS/gpPMm5cfxLmgh6oUYp5dnHVQJSjlokQOsvLbnbCJcqDvGwFU2a3ni" + "\r" + "kV2jAkBjFhMtqpeNU7/XeBMdlDXHxzsUMFLkT88r0iz5FSunuaDhx8WFaGSghqwX" + "\r" + "NrJ+oTPtGzab31zgulfjS4n+FefxAkEAvxDYPphVrQZXv1puI6kUnzt6D8Fd+HEi" + "\r" + "MGl7vbBy4KBg7q3/YTaqZfLrkeHBFefthce/tUCL0CL6GfdOJXNlZwJADdZnbQj3" + "\r" + "xIm27yOS3Zpw5pcR5zflmyHOh/hGN77O7E0rN1hVgLfdMElJkr/b8oCpkdUw3QwO" + "\r" + "5B6GtFdK0XQxlg==" + "\r";

    private static void initURLEncodeMap()
    {
        /*
         * 字符：；、描述：分号、用法：保留、编码：%3B 字符：/ 、描述：斜线、用法：保留、编码：%2F 字符：?
         * 、描述：问号、用法：保留、编码：%3F 字符：: 、描述：冒号、用法：保留、编码：%3A 字符：@
         * 、描述："at"符号、用法：保留、编码：%40 字符：= 、描述：等号、用法：保留、编码：%3D 字符：&
         * 、描述：“和”符号、用法：保留、编码：%26 字符：< 、描述：小于号、用法：不安全、编码：%3C 字符：>
         * 、描述：大于号、用法：不安全、编码：%3E 字符：" 、描述：双引号、用法：不安全、编码：%22 字符：%
         * 、描述：百分号、用法：不安全、编码：%25 字符：{ 、描述：左大括号、用法：不安全、编码：%7B 字符：}
         * 、描述：右大括号、用法：不安全、编码：%7D 字符：| 、描述：竖线、用法：不安全、编码：%7C 字符：\
         * 、描述：反斜线、用法：不安全、编码：%5C 字符：^ 、描述：加子号、用法：不安全、编码：%5E 字符：~
         * 、描述：波浪号、用法：不安全、编码：%7E 字符：[ 、描述：左中括号、用法：不安全、编码：%5B 字符：]
         * 、描述：右中括号、用法：不安全、编码：%5D 字符：` 、描述：反单引号、用法：不安全、编码：%60
         */
        URLEncodeMap.put(';', "%3B");
        URLEncodeMap.put('/', "%2F");
        URLEncodeMap.put('?', "%3F");
        URLEncodeMap.put(':', "%3A");
        URLEncodeMap.put('@', "%40");
        URLEncodeMap.put('=', "%3D");
        URLEncodeMap.put('&', "%26");
        URLEncodeMap.put('<', "%3C");
        URLEncodeMap.put('>', "%3E");
        URLEncodeMap.put('"', "%22");
        URLEncodeMap.put('%', "%25");
        URLEncodeMap.put('{', "%7B");
        URLEncodeMap.put('}', "%7D");
        URLEncodeMap.put('|', "%7C");
        URLEncodeMap.put('\\', "%5C");
        URLEncodeMap.put('^', "%5E");
        URLEncodeMap.put('~', "%7E");
        URLEncodeMap.put('[', "%5B");
        URLEncodeMap.put(']', "%5D");
        URLEncodeMap.put('`', "%60");

    }

    private static void initURLDecodeMap()
    {

        URLDecodeMap.put("%3B", ';');
        URLDecodeMap.put("%2F", '/');
        URLDecodeMap.put("%3F", '?');
        URLDecodeMap.put("%3A", ':');
        URLDecodeMap.put("%40", '@');
        URLDecodeMap.put("%3D", '=');
        URLDecodeMap.put("%26", '&');
        URLDecodeMap.put("%3C", '<');
        URLDecodeMap.put("%3E", '>');
        URLDecodeMap.put("%22", '"');
        URLDecodeMap.put("%25", '%');
        URLDecodeMap.put("%7B", '{');
        URLDecodeMap.put("%7D", '}');
        URLDecodeMap.put("%7C", '|');
        URLDecodeMap.put("%5C", '\\');
        URLDecodeMap.put("%5E", '^');
        URLDecodeMap.put("%7E", '~');
        URLDecodeMap.put("%5B", '[');
        URLDecodeMap.put("%5D", ']');
        URLDecodeMap.put("%60", '`');

    }
    static
    {
        try
        {
            InputStream finPub = CoderUtil.class.getResourceAsStream("rsa_public_key.pem");
            loadPublicKey(finPub);
        }
        catch (FileNotFoundException e)
        {
            logger.warn("" + e);
        }
        catch (Exception e)
        {
            logger.warn("" + e);
        }
    }

    public static RSAPrivateKey getPrivateKey()
    {
        return privateKey;
    }

    public static RSAPublicKey getPublicKey()
    {
        return publicKey;
    }

    /**
     * 将16进制转换为二进制
     * 
     * @param strhex
     * @return
     */
    public static byte[] parseHexStr2Byte(String strhex)
    {
        if (strhex == null)
        {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1)
        {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++)
        {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 将二进制转换成16进制
     * 
     * @param byte[] b
     * @return
     */
    public static String parseByte2HexStr(byte[] b)
    {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
            {
                hs = hs + "0" + stmp;
            }
            else
            {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception
    {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception
    {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * SHA加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception
    {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * MD5加密
     * 
     * @param data
     * @return
     */

    public static String getMD5Code(String data)
    {
        String resultString = null;
        try
        {
            resultString = new String(data);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = bytesToHexString(((md.digest(data.getBytes()))));
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.warn(ex);
        }
        catch (Exception e)
        {
            logger.warn("" + e);
        }
        return resultString;
    }

    /**
     * 用私钥对信息生成数字签名
     * 
     * @param data
     *            加密数据
     * @param privatekey
     *            私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception
    {
        // 解密由BASE64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     * 
     * @param data
     *            加密数据
     * @param publickey
     *            公钥
     * @param sign
     *            数字签名
     * 
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publickey, String sign) throws Exception
    {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publickey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * 解密<br>
     * 用公钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception
    {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception
    {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得密钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用私钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception
    {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception
    {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception
    {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception
    {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     * 
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initkey() throws Exception
    {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 从文件中输入流中加载公钥
     * 
     * @param in
     *            公钥输入流
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static void loadPublicKey(InputStream in) throws Exception
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null)
            {
                if (readLine.charAt(0) == '-')
                {
                    continue;
                }
                else
                {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        }
        catch (IOException e)
        {
            logger.warn("" + e);
        }
        catch (NullPointerException e)
        {
            logger.warn("" + e);
        }
    }

    /**
     * 从字符串中加载公钥
     * 
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static void loadPublicKey(String publicKeyStr) throws Exception
    {
        try
        {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.warn("" + e);
        }
        catch (InvalidKeySpecException e)
        {
            logger.warn("" + e);
        }
        catch (IOException e)
        {
            logger.warn("" + e);
        }
        catch (NullPointerException e)
        {
            logger.warn("" + e);
        }
    }

    /**
     * 从文件中加载私钥
     * 
     * @param keyFileName
     *            私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public static void loadPrivateKey(InputStream in) throws Exception
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null)
            {
                if (readLine.charAt(0) == '-')
                {
                    continue;
                }
                else
                {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        }
        catch (IOException e)
        {
            logger.warn("" + e);
        }
        catch (NullPointerException e)
        {
            logger.warn("" + e);
        }
    }

    public static void loadPrivateKey(String privateKeyStr) throws Exception
    {
        try
        {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.warn("" + e);
            ;
        }
        catch (InvalidKeySpecException e)
        {
            logger.warn("" + e);
        }
        catch (IOException e)
        {
            logger.warn("" + e);
        }
        catch (NullPointerException e)
        {
            logger.warn("" + e);
        }
    }

    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] data) throws Exception
    {
        if (privateKey == null)
        {
            throw new Exception("Public key is empty");
        }
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0)
            {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK)
                {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                }
                else
                {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.warn("" + e);
        }
        catch (NoSuchPaddingException e)
        {
            logger.warn("" + e);
            return null;
        }
        catch (InvalidKeyException e)
        {
            logger.warn("" + e);
        }
        catch (IllegalBlockSizeException e)
        {
            logger.warn("" + e);
        }
        catch (BadPaddingException e)
        {
            logger.warn("" + e);
        }
        return null;
    }

    public static byte[] decrypt(RSAPublicKey publicKey, byte[] data) throws Exception
    {
        if (publicKey == null)
        {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0)
            {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK)
                {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                }
                else
                {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.warn("" + e);
        }
        catch (NoSuchPaddingException e)
        {
            logger.warn("" + e);
            return null;
        }
        catch (InvalidKeyException e)
        {
            logger.warn("" + e);
        }
        catch (IllegalBlockSizeException e)
        {
            logger.warn("" + e);
        }
        catch (BadPaddingException e)
        {
            logger.warn("" + e);
        }
        return null;
    }

    public static String myURLEncoder(String input)
    {

        if (URLEncodeMap.size() == 0)
        {
            initURLEncodeMap();
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < input.length(); i++)
        {
            Character c = Character.valueOf(input.charAt(i));
            stringBuffer.append(URLEncodeMap.containsKey(c) ? URLEncodeMap.get(c) : c.charValue());
        }

        return stringBuffer.toString();

    }

    public static String myURLDecoder(String input)
    {

        if (URLDecodeMap.size() == 0)
        {
            initURLDecodeMap();
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < input.length();)
        {
            Character c = Character.valueOf(input.charAt(i));
            if (c.charValue() != '%')
            {
                stringBuffer.append(c);
                i++;
            }
            else
            {
                stringBuffer.append(URLDecodeMap.get(input.substring(i, i + 3)).charValue());
                i += 3;
            }
        }

        return stringBuffer.toString();
    }

    /**
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src
     *            byte[] data
     * @return hex string
     **/
    public static String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString)
    {
        if (hexString == null || hexString.equals(""))
        {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
