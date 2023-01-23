package com.ghazalpaknia_mahdinazarian.static_values;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helpers {
    public static String byteConverter(Long byteSize,boolean isSpeed){
        String suffix;
        float divisionVal;
        if(byteSize < 1000) {
            suffix = (isSpeed)?"B/s":"B";
            return byteSize + suffix;
        }
        else if(byteSize >=1000 && byteSize < 1000000){
            suffix = (isSpeed)?"KB/s":"KB";
            divisionVal = byteSize / 1000f;
            return String.format("%.1f "+suffix,divisionVal);
        }else if(byteSize >= 1000000 && byteSize < 1000000000){
            suffix = (isSpeed)?"MB/s":"MB";
            divisionVal = byteSize / 1000000f;
            return String.format("%.1f "+suffix,divisionVal);
        }else{
            suffix = (isSpeed)?"GB/s":"GB";
            divisionVal = byteSize / 1000000000f;
            return String.format("%.1f "+suffix,divisionVal);
        }
    }
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
