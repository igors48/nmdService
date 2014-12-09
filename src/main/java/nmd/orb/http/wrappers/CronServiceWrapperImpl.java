package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServiceWrapperImpl implements CronServiceWrapper {

    @Override
    public ResponseBody executeCronJobs() {
        return null;
    }

    /*
        private static final long UPDATE_PERIOD = 9000;

    @Override
    public ResponseBody updateCurrentFeeds() {
        final Quota quota = new TimeQuota(UPDATE_PERIOD);
        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(quota);
        final FeedSeriesUpdateResponse response = convert(report);

        return createJsonResponse(response);
    }


     */
}
