package cn.shawda.ratelimiter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SlidingWindowWithArrayTest {

    @Test
    public void isRateLimitWhenOverThreshold() {
        SlidingWindowWithArray slidingWindowWithArray = new SlidingWindowWithArray(10);
        for (int i = 0; i < 10; i++) {
            assertFalse(slidingWindowWithArray.isRateLimit());
        }

        assertTrue(slidingWindowWithArray.isRateLimit());
        assertTrue(slidingWindowWithArray.isRateLimit());
        assertTrue(slidingWindowWithArray.isRateLimit());
    }

    @Test
    public void testIsRateLimitWhenAlwaysOK() throws InterruptedException {
        SlidingWindowWithArray slidingWindowWithArray = new SlidingWindowWithArray(10);
        for (int i = 0; i < 20; i++) {
            Thread.sleep(301L);
            assertFalse(slidingWindowWithArray.isRateLimit());
        }
    }
}
