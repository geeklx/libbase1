package com.geek.libencryption.Utils;

/**
 * Created by wyf on 2017/8/22.
 *
 * SHA256加密工具类
 */

public class SHA256Utils {

    private SHA256Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * SHA256加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public static String encrypt2String(final String data) {
        return encrypt2String(data.getBytes());
    }

    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */
    public static String encrypt2String(final byte[] data) {
        return HexUtils.bytes2HexString(encrypt(data));
    }

    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encrypt(final byte[] data) {
        return EncryptionTemplate.hashTemplate(data, "SHA256");
    }

    /**
     * HmacSHA256加密
     *
     * @param data 明文字符串
     * @param key  秘钥
     * @return 16进制密文
     */
    public static String encryptHmac2String(final String data, final String key) {
        return encryptHmac2String(data.getBytes(), key.getBytes());
    }

    /**
     * HmacSHA256加密
     *
     * @param data 明文字节数组
     * @param key  秘钥
     * @return 16进制密文
     */
    public static String encryptHmac2String(final byte[] data, final byte[] key) {
        return HexUtils.bytes2HexString(encryptHmac(data, key));
    }

    /**
     * HmacSHA256加密
     *
     * @param data 明文字节数组
     * @param key  秘钥
     * @return 密文字节数组
     */
    public static byte[] encryptHmac(final byte[] data, final byte[] key) {
        return EncryptionTemplate.hmacTemplate(data, key, "HmacSHA256");
    }

}