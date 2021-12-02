package com.amsabots.jenzi.fundi_service.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
public class Commons {
    public static String randomHexValue() {
        return Integer.toHexString(ThreadLocalRandom.current().nextInt(150, 255 + 1));
    }
    public static String createRandomColor() {
        return String.format("#%s%s%s", randomHexValue(), randomHexValue(), randomHexValue());
    }

    public static String randomSmallHexValue() {
        return Integer.toHexString(ThreadLocalRandom.current().nextInt(30, 120));
    }

    public static String createRandomDeepColor() {
        return String.format("#%s%s%s", randomSmallHexValue(), randomSmallHexValue(), randomSmallHexValue());
    }

}
