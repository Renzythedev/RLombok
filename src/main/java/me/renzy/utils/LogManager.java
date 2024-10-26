package me.renzy.utils;

import javax.annotation.Nonnull;

public final class LogManager {

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";

    public static void info(@Nonnull String info) {
        System.out.println("LOGGER " + BLUE + "INFO " + RESET + info +RESET);
    }

    public static void warn(@Nonnull String warn) {
        System.out.println("LOGGER " + YELLOW + "WARN " +RESET + warn +RESET);
    }

    public static void err(@Nonnull String error) {
        System.out.println("LOGGER " +RED + "ERROR " + RESET + error + RESET);
    }

    private LogManager() {}
}
