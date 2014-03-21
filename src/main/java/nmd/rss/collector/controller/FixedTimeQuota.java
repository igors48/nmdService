package nmd.rss.collector.controller;

import static java.lang.System.currentTimeMillis;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * @author : igu
 */
public class FixedTimeQuota implements TimeQuota {

    private final long startTime;
    private final long period;

    public FixedTimeQuota(final long period) {
        assertPositive(period);
        this.period = period;

        this.startTime = currentTimeMillis();
    }

    @Override
    public boolean hasTime() {
        final long currentTime = currentTimeMillis();

        return currentTime - this.startTime <= this.period;
    }

}
