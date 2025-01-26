package dev.joon.hakko.stream;

public enum Platform {
    CHZZK,
    SOOP;

    public static Platform from(String platform) throws IllegalArgumentException {
        return Platform.valueOf(platform.toUpperCase());
    }
}
