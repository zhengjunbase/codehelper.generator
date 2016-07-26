package com.ccnode.codegenerator.util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 */
public class SecurityUtil {
    private static final String defaultCharset = "UTF-8";
    private static final Logger logger = LoggerWrapper.getLogger(SecurityUtil.class);

    public static class MD5 {
        /**
         * Returns a MessageDigest for the given <code>algorithm</code>.
         *
         * @param
         * @return An MD5 digest instance.
         * @throws RuntimeException when a {@link NoSuchAlgorithmException} is
         *                          caught
         */
        static MessageDigest getDigest() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Calculates the MD5 digest and returns the value as a 16 element
         * <code>byte[]</code>.
         *
         * @param data Data to digest
         * @return MD5 digest
         */
        public static byte[] md5(byte[] data) {
            return getDigest().digest(data);
        }

        /**
         * Calculates the MD5 digest and returns the value as a 16 element
         * <code>byte[]</code>.
         *
         * @param data Data to digest
         * @return MD5 digest
         */
        public static byte[] md5(String data) {
            return md5(data.getBytes());
        }

        /**
         * Calculates the MD5 digest and returns the value as a 32 character hex
         * string.
         *
         * @param data Data to digest
         * @return MD5 digest as a hex string
         */
        public static String md5Hex(byte[] data) {
            return HexUtil.toHexString(md5(data));
        }

        /**
         * Calculates the MD5 digest and returns the value as a 32 character hex
         * string.
         *
         * @param data Data to digest
         * @return MD5 digest as a hex string
         */
        public static String md5Hex(String data) {
            return HexUtil.toHexString(md5(data));
        }
    }

    public static class AES {
        private static final String KEY_AES = "AES";

        /**
         * 加密
         *
         * @param data 需要加密的内容
         * @param key  加密密码
         * @return
         */
        @Nullable
        public static String encrypt(String data, String key) {
            return doAES(data, key, Cipher.ENCRYPT_MODE);
        }

        /**
         * 解密
         *
         * @param data 待解密内容
         * @param key  解密密钥
         * @return
         */
        @Nullable
        public static String decrypt(String data, String key) {
            return doAES(data, key, Cipher.DECRYPT_MODE);
        }

        /**
         * 加解密
         *
         * @param data
         * @param key
         * @param mode
         * @return
         */
        @Nullable
        private static String doAES(String data, String key, int mode) {
            try {
                if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                    return null;
                }
                boolean encrypt = mode == Cipher.ENCRYPT_MODE;
                byte[] content;
                if (encrypt) {
                    content = data.getBytes(defaultCharset);
                } else {
                    content = Base64.decodeBase64(data);
                }
                SecretKeySpec keySpec = new SecretKeySpec(MD5.md5(key.getBytes(defaultCharset)), KEY_AES);
                Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器
                cipher.init(mode, keySpec);// 初始化
                byte[] result = cipher.doFinal(content);
                if (encrypt) {
                    return Base64.encodeBase64String(result);
                } else {
                    return new String(result, defaultCharset);
                }
            } catch (Exception e) {
                logger.error("AES密文处理异常", e);
            }
            return null;
        }
    }
}
