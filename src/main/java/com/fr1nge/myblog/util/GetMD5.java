package com.fr1nge.myblog.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetMD5 {
    public static String encryptString(String password) {
        MessageDigest messageDigest;
        try {
            byte[] bytes = password.trim().getBytes();
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] MD5_bytes = messageDigest.digest(bytes);
            return toHexStr(MD5_bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String encryptFile(InputStream stream) throws IOException, NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexStr(digest.digest());
        } catch (Exception e) {
            throw e;
        }
    }

    private static String toHexStr(byte[] MD5_bytes) {
        int digital;
        StringBuilder md5str = new StringBuilder();
        for (byte md5_byte : MD5_bytes) {//把数组中每一字节换成16进制连成md5字符串
            digital = md5_byte;
            if (digital < 0) {//16及以上的数转16进制是两位
                digital += 256;
            }
            if (digital < 16) {//如果是0~16之间的数的话则变成0X
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    public static void main(String[] args) {
        String a = GetMD5.encryptString("liuhs?2021");
        System.out.println(a);
    }
}
