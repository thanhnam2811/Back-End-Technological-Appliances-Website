package com.hcmute.backendtechnologicalapplianceswebsite.utils;

public class MyUtils {
    public static String generateID(String PREFIX, int length, String lastIdStr) {
        long lastId = Long.parseLong(lastIdStr.substring(PREFIX.length()));

        while (lastId >= Math.pow(10, length))
            length = length + 1;

        String format = "%0" + length + "d";

        return PREFIX + String.format(format, lastId + 1);
    }
}
