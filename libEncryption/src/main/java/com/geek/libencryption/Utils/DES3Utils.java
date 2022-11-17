package com.geek.libencryption.Utils;

/**
 * Created by wyf on 2017/8/22.
 *
 * 3DES加密和解密的工具类
 */

public class DES3Utils {

    private DES3Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 3DES转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    public static String TripleDES_Transformation = "DESede/ECB/PKCS5Padding";
    private static final String TripleDES_Algorithm = "DESede";


    /**
     * 3DES加密后转为Base64编码
     *
     * @param data 明文
     * @param key  24字节秘钥
     * @return Base64密文
     */
    public static byte[] encrypt2Base64(final byte[] data, final byte[] key) {
        return Base64.encode(encrypt(data, key)).getBytes();
    }

    /**
     * 3DES加密后转为16进制
     *
     * @param data 明文
     * @param key  24字节秘钥
     * @return 16进制密文
     */
    public static String encrypt2HexString(final byte[] data, final byte[] key) {
        return HexUtils.bytes2HexString(encrypt(data, key));
    }

    /**
     * 3DES加密
     *
     * @param data 明文
     * @param key  24字节密钥
     * @return 密文
     */
    public static byte[] encrypt(final byte[] data, final byte[] key) {
        return EncryptionTemplate.desTemplate(data, key, TripleDES_Algorithm, TripleDES_Transformation, true);
    }

    /**
     * 3DES解密Base64编码密文
     *
     * @param data Base64编码密文
     * @param key  24字节秘钥
     * @return 明文
     */
    public static byte[] decryptBase64(final byte[] data, final byte[] key) {
        return decrypt(Base64.decode(new String(data)), key);
    }

    /**
     * 3DES解密16进制密文
     *
     * @param data 16进制密文
     * @param key  24字节秘钥
     * @return 明文
     */
    public static byte[] decryptHexString(final String data, final byte[] key) {
        return decrypt(HexUtils.hexString2Bytes(data), key);
    }

    /**
     * 3DES解密
     *
     * @param data 密文
     * @param key  24字节密钥
     * @return 明文
     */
    public static byte[] decrypt(final byte[] data, final byte[] key) {
        return EncryptionTemplate.desTemplate(data, key, TripleDES_Algorithm, TripleDES_Transformation, false);
    }

}
