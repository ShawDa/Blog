package cn.shawda.ratelimiter;

import junit.framework.TestCase;

public class SlidingWindowWithQueueTest extends TestCase {

    public void testIsRateLimitWhenOverThreshold() {
        SlidingWindowWithQueue slidingWindowWithQueue = new SlidingWindowWithQueue();
        for (int i = 0; i < 10; i++) {
            assertFalse(slidingWindowWithQueue.isRateLimit());
        }

        assertTrue(slidingWindowWithQueue.isRateLimit());
    }

    public void testIsRateLimitWhenAlwaysOK() throws InterruptedException {
        SlidingWindowWithQueue slidingWindowWithQueue = new SlidingWindowWithQueue();
        for (int i = 0; i < 20; i++) {
            Thread.sleep(301L);
            assertFalse(slidingWindowWithQueue.isRateLimit());
        }
    }
}
