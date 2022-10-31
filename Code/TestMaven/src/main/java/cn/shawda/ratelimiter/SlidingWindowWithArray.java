package cn.shawda.ratelimiter;

import java.util.Arrays;

public class SlidingWindowWithArray {
    // 窗口大小
    private static final long PERIOD = 3000L;

    // 限流阈值
    private static final int THRESHOLD = 10;

    // 数组实现的滑动窗口，循环
    private final long[] slidingWindow;

    private final long startTimestamp;

    private final long windowPeriod;

    private long currentIndex;

    public SlidingWindowWithArray(int windowCount) {
        slidingWindow = new long[windowCount];
        startTimestamp = System.currentTimeMillis();
        windowPeriod = PERIOD / windowCount;
    }

    /*
     * 数组和就是过去一个周期的请求数目
     */
    public boolean isRateLimit() {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - startTimestamp;
        long index = interval / windowPeriod;

        long noRequestWindowLength = index - currentIndex;
        if (noRequestWindowLength >= slidingWindow.length) {
            currentIndex = index;
            Arrays.fill(slidingWindow, 0L);
            slidingWindow[(int) (currentIndex % slidingWindow.length)]++;
            return false;
        }

        if (noRequestWindowLength == 0) {
            slidingWindow[(int) (currentIndex % slidingWindow.length)]++;
        } else {
            for (int i = 1; i < noRequestWindowLength; i++) {
                slidingWindow[(int) ((currentIndex + i) % slidingWindow.length)] = 0;
            }
            slidingWindow[(int) ((currentIndex + noRequestWindowLength) % slidingWindow.length)] = 1;
        }

        return Arrays.stream(slidingWindow).sum() > THRESHOLD;
    }
}
