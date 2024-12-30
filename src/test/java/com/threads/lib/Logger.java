package com.threads.lib;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Logger {
    public record LocalLogger(Consumer<String> logFn, Consumer<String> errFn, Runnable closeFn) {
        public void log(String message) { logFn.accept(message); }
        public void err(String message) { errFn.accept(message); }
        public void close() { closeFn.run(); }
    }

    private static Logger instance;
    private Logger() {}
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private final String resetColor = "\u001B[0m";
    private final String errorColor = "\u001B[31m";
    private static final List<String> colorList = new LinkedList<>(Arrays.asList(
            "\u001B[32m", // green
            "\u001B[33m", // yellow
            "\u001B[34m", // blue
            "\u001B[35m", // purple
            "\u001B[36m" // cyan
    ));

    public LocalLogger getLocalLogger(String loggerTag) {
        synchronized (colorList) {
            var color = colorList.isEmpty() ? resetColor : colorList.remove(0);
            return new LocalLogger(
                    message -> log(color, loggerTag, message),
                    message -> error(color, loggerTag, message),
                    () -> close(color));
        }
    }

    public void close(String color) {
        if (!Objects.equals(color, resetColor)) {
            synchronized (colorList) {
                colorList.add(0, color);
            }
        }
    }

    private void log(String color, String testName, String message) {
        System.out.println(
                color + "[" + testName + "-" + Thread.currentThread().getId() + "] " + resetColor + message);
    }

    private void error(String color, String testName, String message) {
        System.out.println(
                color + "[" + testName + "-" + Thread.currentThread().getId() + "] " + errorColor + message + resetColor);
    }
}
