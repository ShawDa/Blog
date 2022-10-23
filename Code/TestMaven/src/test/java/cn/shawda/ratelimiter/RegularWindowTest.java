package cn.shawda.ratelimiter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegularWindowTest {

    @Test
    public void isRateLimitWhen10RequestsPerPeriod() throws InterruptedException {
        RegularWindow regularWindow = new RegularWindow();
        for (int i = 0; i < 10; i++) {
            boolean isLimit = regularWindow.isRateLimit();
            assertFalse(isLimit);
        }

        Thread.sleep(3100L);
        for (int i = 0; i < 10; i++) {
            boolean isLimit = regularWindow.isRateLimit();
            assertFalse(isLimit);
        }
    }

    @Test
    public void isRateLimitWhenOver10RequestsPerPeriod() throws InterruptedException {
        RegularWindow regularWindow = new RegularWindow();
        for (int i = 0; i < 10; i++) {
            boolean isLimit = regularWindow.isRateLimit();
            assertFalse(isLimit);
        }

        Thread.sleep(1000L);
        for (int i = 0; i < 10; i++) {
            boolean isLimit = regularWindow.isRateLimit();
            assertTrue(isLimit);
        }
    }
}