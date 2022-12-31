package com.ghazalpaknia_mahdinazarian.static_values;

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
}
