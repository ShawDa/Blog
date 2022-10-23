package cn.shawda.ratelimiter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * 固定窗口
 */
public class RegularWindow {
    // 窗口大小
    private static final long PERIOD = 3000L;

    // 限流阈值
    private static final int THRESHOLD = 10;

    // 记录周期内请求数量
    private final AtomicInteger count;

    // 记录状态，避免限流了还在一直add count冗余操作
    private final AtomicBoolean isLimit;

    // 本周期的开始时间，一直在刷新
    private long startTime;

    public RegularWindow() {
        count = new AtomicInteger(0);
        isLimit = new AtomicBoolean(false);
        startTime = System.currentTimeMillis();
    }

    public boolean isRateLimit() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - startTime > PERIOD) {
            count.set(0);
            isLimit.set(false);
            startTime = currentTimeMillis;
        } else if (isLimit.get()) {
            return true;
        }

        if (count.incrementAndGet() > THRESHOLD) {
            isLimit.set(true);
            return true;
        } else {
            return false;
        }
    }
}
