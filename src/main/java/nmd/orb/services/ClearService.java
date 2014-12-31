package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.feed.FeedHeader;
import nmd.orb.repositories.*;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * @author : igu
 */
public class ClearService {

    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final CategoriesRepository categoriesRepository;
    private final ImportJobContextRepository importJobContextRepository;
    private final Transactions transactions;

    public ClearService(FeedHeadersRepository feedHeadersRepository, FeedItemsRepository feedItemsRepository, FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, FeedUpdateTaskRepository feedUpdateTaskRepository, ReadFeedItemsRepository readFeedItemsRepository, CategoriesRepository categoriesRepository, final ImportJobContextRepository importJobContextRepository, Transactions transactions) {
        this.feedHeadersRepository = feedHeadersRepository;
        this.feedItemsRepository = feedItemsRepository;
        this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository;
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;
        this.readFeedItemsRepository = readFeedItemsRepository;
        this.categoriesRepository = categoriesRepository;
        this.importJobContextRepository = importJobContextRepository;
        this.transactions = transactions;
    }

    public void clear() {

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedHeader> backedHeaders = new ArrayList<>(headers);

            for (final FeedHeader header : backedHeaders) {
                FeedsService.removeFeedComponents(header.id, this.feedUpdateTaskRepository, this.feedHeadersRepository, this.feedItemsRepository, this.readFeedItemsRepository);
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
