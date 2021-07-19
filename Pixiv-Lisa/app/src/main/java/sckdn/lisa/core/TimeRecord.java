package sckdn.lisa.core;

import sckdn.lisa.utils.Common;

public class TimeRecord {

    public static long startTime = 0L;
    public static long endTime = 0L;

    public static void start() {
        startTime = 0L;
        Common.showLog("TimeRecord start " + System.nanoTime());
        startTime = System.nanoTime();
    }

    public static void end() {
        endTime = 0L;
        Common.showLog("TimeRecord end " + System.nanoTime());
        endTime = System.nanoTime();
    }

    public static void result() {
        final long temp = endTime - startTime;
        Common.showLog("TimeRecord result 毫秒：" + temp/1000000L);
    }
}
