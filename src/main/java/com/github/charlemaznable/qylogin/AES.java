package com.github.charlemaznable.qylogin;

import com.github.charlemaznable.core.lang.Rand;
import lombok.SneakyThrows;
import lombok.val;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.github.charlemaznable.core.codec.Base64.Format.UrlSafe;
import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Base64.unBase64;
import static com.github.charlemaznable.core.codec.Bytes.bytes;
import static com.github.charlemaznable.core.codec.Bytes.string;
import static java.lang.System.arraycopy;

public class AES {

    private static final String KEY_ALGORITHM = "AES";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final int BLOCK_SIZE = 16;

    public static String encryptBase64(String value, String keyString) {
        return base64(encrypt(value, keyString), UrlSafe);
    }

    public static String decryptBase64(String value, String keyString) {
        return decrypt(unBase64(value), keyString);
    }

    @SneakyThrows
    public static byte[] encrypt(String value, String keyString) {
        val key = paddingkey(keyString);
        val keySpec = secretKeySpec(bytes(key));
        val iv = ivParameterSpec(bytes(Rand.randAscii(16)));

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        val encrypted = cipher.doFinal(bytes(value));

        val result = new byte[BLOCK_SIZE + encrypted.length];
        arraycopy(iv.getIV(), 0, result, 0, BLOCK_SIZE);
        arraycopy(encrypted, 0, result, BLOCK_SIZE, encrypted.length);
        return result;
    }

    @SneakyThrows
    public static String decrypt(byte[] value, String keyString) {
        val ivBytes = new byte[BLOCK_SIZE];
        val target = new byte[value.length - BLOCK_SIZE];
        arraycopy(value, 0, ivBytes, 0, BLOCK_SIZE);
        arraycopy(value, BLOCK_SIZE, target, 0, target.length);

        val key = paddingkey(keyString);
        val keySpec = secretKeySpec(bytes(key));
        val iv = ivParameterSpec(ivBytes);

        val cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        return string(cipher.doFinal(target));
    }

    private static SecretKeySpec secretKeySpec(byte[] bytes) {
        val buf = new byte[BLOCK_SIZE];
        for (int i = 0; i < bytes.length && i < buf.length; i++) {
            buf[i] = bytes[i];
        }
        return new SecretKeySpec(buf, KEY_ALGORITHM);
    }

    private static IvParameterSpec ivParameterSpec(byte[] bytes) {
        return new IvParameterSpec(bytes);
    }

    private static String paddingkey(String originKey) {
        val sb = new StringBuilder(originKey);
        for (int i = originKey.length(); i < BLOCK_SIZE; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}
