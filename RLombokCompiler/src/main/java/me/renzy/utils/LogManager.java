package me.renzy.utils;

public final class LogManager {

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";

    public static void info(String info, Object... format) {
        System.out.println("LOGGER " + BLUE + "INFO " + RESET + String.format(info,format) +RESET);
    }

    public static void warn(String warn, Object... format) {
        System.out.println("LOGGER " + YELLOW + "WARN " +RESET + String.format(warn,format) +RESET);
    }

    public static void err(String error, Object... format) {
        System.out.println("LOGGER " +RED + "ERROR " + RESET + String.format(error,format) + RESET);
    }


    private LogManager(){}
}
