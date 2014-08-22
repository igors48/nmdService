package nmd.rss.http.wrappers;

import nmd.rss.collector.controller.FeedsService;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.exporter.FeedExporterException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.gae.GaeServices;
import nmd.rss.http.responses.FeedHeadersResponse;
import nmd.rss.http.responses.FeedIdResponse;
import nmd.rss.http.tools.ContentType;
import nmd.rss.http.tools.ResponseBody;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static nmd.rss.collector.exporter.FeedExporter.export;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.http.responses.FeedHeadersResponse.convert;
import static nmd.rss.http.responses.SuccessMessageResponse.create;
import static nmd.rss.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.http.tools.ResponseBody.createJsonResponse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServiceWrapperImpl implements FeedsServiceWrapper {

    public static final FeedsServiceWrapperImpl FEEDS_SERVICE_WRAPPER = new FeedsServiceWrapperImpl(GaeServices.FEEDS_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(FeedsServiceWrapperImpl.class.getName());

    private final FeedsService feedsService;

    public FeedsServiceWrapperImpl(final FeedsService feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    @Override
    public ResponseBody addFeed(final String feedUrl, final String categoryId) {

        try {
            final UUID feedId = this.feedsService.addFeed(feedUrl, categoryId);

            final FeedIdResponse feedIdResponse = FeedIdResponse.create(feedId);

            LOGGER.info(format("Feed [ %s ] added to category [ %s ]. Id is [ %s ]", feedUrl, categoryId, feedId));

            return createJsonResponse(feedIdResponse);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error adding feed [ %s ]", feedUrl), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody updateFeedTitle(final UUID feedId, final String title) {

        try {
            this.feedsService.updateFeedTitle(feedId, title);

            final String message = format("Feeds [ %s ] title changed to [ %s ]", feedId, title);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error changing feed [ %s ] title", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody removeFeed(final UUID feedId) {
        this.feedsService.removeFeed(feedId);

        final String message = format("Feed [ %s ] removed", feedId);

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    @Override
    public ResponseBody getFeedHeaders() {
        final List<FeedHeader> headers = this.feedsService.getFeedHeaders();
        final FeedHeadersResponse feedHeadersResponse = convert(headers);

        LOGGER.info(format("[ %s ] feed headers found", headers.size()));

        return createJsonResponse(feedHeadersResponse);
    }

    @Override
    public ResponseBody getFeedHeader(final UUID feedId) {

        try {
            final FeedHeader header = this.feedsService.loadFeedHeader(feedId);
            final FeedHeadersResponse response = convert(asList(header));

            LOGGER.info(format("Header for feed [ %s ] returned", feedId));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error loading feed [ %s ] header", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody getFeed(final UUID feedId) {

        try {
            final Feed feed = this.feedsService.getFeed(feedId);
            final String feedAsXml = export(feed.header, feed.items);

            LOGGER.info(format("Feed [ %s ] link [ %s ] items exported. Items count [ %d ]", feedId, feed.header.feedLink, feed.items.size()));

            return new ResponseBody(ContentType.XML, feedAsXml);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error export feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(exception);
        } catch (FeedExporterException exception) {
            LOGGER.log(Level.SEVERE, format("Error export feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(ServiceError.feedExportError(feedId));
        }
    }

    @Override
    public ResponseBody clear() {
        this.feedsService.clear();

        final String message = "Service cleared";

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

}
