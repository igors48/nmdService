package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;

/**
 * Created by igor on 09.12.2014.
 */
public interface CronServiceWrapper {

    ResponseBody executeCronJobs();

}
