package nmd.orb.collector.fetcher;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public interface UrlFetcher {

    byte[] fetch(String link) throws UrlFetcherException;

    String fetchString(String link) throws UrlFetcherException;

}
