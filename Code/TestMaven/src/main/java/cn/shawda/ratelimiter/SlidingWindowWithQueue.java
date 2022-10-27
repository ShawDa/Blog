package cn.shawda.ratelimiter;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * 用队列实现的滑动窗口
 */
public class SlidingWindowWithQueue {
    // 窗口大小
    private static final long PERIOD = 3000L;

    // 限流阈值
    private static final int THRESHOLD = 10;

    // 队列实现的滑动窗口
    private final Queue<Long> slidingWindow;

    public SlidingWindowWithQueue() {
        slidingWindow = new LinkedBlockingQueue<>();
    }

    /*
     * 将问题简化成：请求来了之后看过去这个周期是否有超出阈值的请求进来
     */
    public boolean isRateLimit() {
        long currentTimeMillis = System.currentTimeMillis();
        Long earliestTime = slidingWindow.peek();
        while (earliestTime != null && earliestTime < currentTimeMillis - PERIOD) {
            slidingWindow.remove();
            earliestTime = slidingWindow.peek();
        }

        if (slidingWindow.size() < THRESHOLD) {
            slidingWindow.add(currentTimeMillis);
            return false;
        } else {
            return true;
        }
    }
}
