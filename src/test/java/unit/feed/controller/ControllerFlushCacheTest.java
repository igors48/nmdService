package unit.feed.controller;

import nmd.orb.error.ServiceException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ControllerFlushCacheTest extends AbstractControllerTestBase {

    @Before
    @Override
    public void before() throws ServiceException {
        super.before();

        this.cacheStub.put("key", "value");
    }

    @Test
    public void whenFlushedThenCacheIsEmpty() {
        assertFalse(this.cacheStub.isEmpty());

        this.administrationService.flushCache();

        assertTrue(this.cacheStub.isEmpty());
    }

}
