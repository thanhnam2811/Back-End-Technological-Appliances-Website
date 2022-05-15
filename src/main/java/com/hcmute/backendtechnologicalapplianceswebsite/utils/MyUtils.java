package com.hcmute.backendtechnologicalapplianceswebsite.utils;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j
public class MyUtils {
    public static String generateID(String PREFIX, int length, String lastIdStr) {
        long lastId = Long.parseLong(lastIdStr.substring(PREFIX.length()));

        while (lastId >= Math.pow(10, length))
            length = length + 1;

        String format = "%0" + length + "d";

        return PREFIX + String.format(format, lastId + 1);
    }

    public static String generateToken(int length) {
        String generatedString = RandomString.make(length);
        log.info("Generated token: " + generatedString);
        return generatedString;
    }

    public static String generateToken() {
        return generateToken(60);
    }
}
