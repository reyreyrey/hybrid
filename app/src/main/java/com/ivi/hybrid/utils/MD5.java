package com.ivi.hybrid.utils;

import java.security.MessageDigest;

/**
 * Created by Rea.X on 2017/2/1.
 * <p>MD5工具类</p>
 */

public class MD5 {

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte [] b = md.digest();
            int i;
            StringBuffer sb = new StringBuffer();
            for(int offset=0;offset<b.length;offset++){
                i = b[offset];
                if(i<0)i+=256;
                if(i<16)sb.append("0");
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }

}
