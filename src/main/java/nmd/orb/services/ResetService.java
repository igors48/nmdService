package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.feed.FeedHeader;
import nmd.orb.repositories.*;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * @author : igu
 */
public class ResetService {

    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final CategoriesRepository categoriesRepository;
    private final ImportJobContextRepository importJobContextRepository;
    private final AutoExportService autoExportService;
    private final Transactions transactions;

    public ResetService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final CategoriesRepository categoriesRepository, final ImportJobContextRepository importJobContextRepository, final AutoExportService autoExportService, final Transactions transactions) {
        guard(notNull(feedHeadersRepository));
        this.feedHeadersRepository = feedHeadersRepository;

        guard(notNull(feedItemsRepository));
        this.feedItemsRepository = feedItemsRepository;

        guard(notNull(feedUpdateTaskSchedulerContextRepository));
        this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository;

        guard(notNull(feedUpdateTaskRepository));
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        guard(notNull(readFeedItemsRepository));
        this.readFeedItemsRepository = readFeedItemsRepository;

        guard(notNull(categoriesRepository));
        this.categoriesRepository = categoriesRepository;

        guard(notNull(importJobContextRepository));
        this.importJobContextRepository = importJobContextRepository;

        guard(notNull(autoExportService));
        this.autoExportService = autoExportService;

        guard(notNull(transactions));
        this.transactions = transactions;
    }

    public void reset() {

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedHeader> backedHeaders = new ArrayList<>(headers);

            for (final FeedHeader header : backedHeaders) {
                FeedsService.removeFeedComponents(header.id, this.feedUpdateTaskRepository, this.feedHeadersRepository, this.feedItemsRepository, this.readFeedItemsRepository, this.autoExportService);
            }

            this.feedUpdateTaskSchedulerContextRepository.clear();

            this.categoriesRepository.clear();

            this.importJobContextRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }


}
