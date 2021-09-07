package com.fr1nge.myblog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetMD5 {
    public static String encryptString(String password) {
        MessageDigest messageDigest = null;
        StringBuilder md5str = new StringBuilder();
        int digital = 0;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = password.trim().getBytes();
            byte[] MD5_bytes = messageDigest.digest(bytes);
            //将字节数组转化为16进制字符串
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
