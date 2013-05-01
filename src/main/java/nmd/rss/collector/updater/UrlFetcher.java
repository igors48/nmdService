package nmd.rss.collector.updater;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public interface UrlFetcher {

    String fetch(String link) throws UrlFetcherException;

}
