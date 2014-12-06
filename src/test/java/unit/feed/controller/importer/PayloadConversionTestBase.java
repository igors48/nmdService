package unit.feed.controller.importer;

import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.payload.BackupReportPayload;
import nmd.orb.http.responses.payload.CategoryPayload;
import nmd.orb.http.responses.payload.FeedHeaderPayload;
import nmd.orb.reader.Category;
import org.junit.Before;

import java.util.*;

/**
 * Created by igor on 06.12.2014.
 */
public class PayloadConversionTestBase {

    protected static final int TRIES_COUNT = 3;
    protected static final String NAME = "name";
    protected static final String HTTP_DOMAIN_COM = "http://domain.com";
    protected static final String TITLE = "title";
    protected static final String DESCRIPTION = "description";

    protected List<BackupReportPayload> backupReportPayloads;
    protected CategoryPayload categoryPayload;
    protected Set<FeedHeaderPayload> feedHeaderPayloads;
    protected FeedHeaderPayload feedHeaderPayload;
    protected FeedHeader feedHeader;
    protected Category category;
    protected Set<FeedHeader> feedHeaders;

    @Before
    public void setUp() {
        createFeedHeaderPayload();
        createFeedHeaderPayloads();
        createCategoryPayload();
        createBackupReportPayloads();
    }

    private void createBackupReportPayloads() {
        this.feedHeaders = new HashSet<>();
        this.feedHeaders.add(this.feedHeader);

        this.backupReportPayloads = new ArrayList<>();
        final BackupReportPayload backupReportPayload = BackupReportPayload.create(this.category, this.feedHeaders);
        this.backupReportPayloads.add(backupReportPayload);
    }

    private void createFeedHeaderPayloads() {
        this.feedHeaderPayloads = new HashSet<>();
        this.feedHeaderPayloads.add(this.feedHeaderPayload);
    }

    private void createFeedHeaderPayload() {
        this.feedHeader = new FeedHeader(UUID.randomUUID(), HTTP_DOMAIN_COM, TITLE, DESCRIPTION, HTTP_DOMAIN_COM);
        this.feedHeaderPayload = FeedHeaderPayload.create(this.feedHeader);
    }

    private void createCategoryPayload() {
        this.category = new Category(UUID.randomUUID().toString(), NAME);
        this.categoryPayload = CategoryPayload.create(this.category);
    }

}
