package com.github.charlemaznable.qylogin;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AESTest {

    @Test
    public void testAES() {
        val plainText = "abc中国汉字1234567#$%^&*(";

        String keyString = "A916EFFC3121F935";
        String encrypt = AES.encryptBase64(plainText, keyString);
        String decrypt = AES.decryptBase64(encrypt, keyString);
        assertEquals(plainText, decrypt);

        keyString = "A916EFFC3121F93";
        encrypt = AES.encryptBase64(plainText, keyString);
        decrypt = AES.decryptBase64(encrypt, keyString);
        assertEquals(plainText, decrypt);

        keyString = "A916EFFC3121F9353";
        encrypt = AES.encryptBase64(plainText, keyString);
        decrypt = AES.decryptBase64(encrypt, keyString);
        assertEquals(plainText, decrypt);
    }
}
