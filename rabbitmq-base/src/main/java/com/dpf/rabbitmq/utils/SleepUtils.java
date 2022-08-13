package com.dpf.rabbitmq.utils;

/**
 * @Author dpf
 * @Since 2021/7/11
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
