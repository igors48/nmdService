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
    private final ChangeRegistrationService changeRegistrationService;
    private final UpdateErrorRegistrationService updateErrorRegistrationService;
    private final ChangeRepository changeRepository;
    private final Transactions transactions;

    public ResetService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final CategoriesRepository categoriesRepository, final ImportJobContextRepository importJobContextRepository, final ChangeRepository changeRepository, final ChangeRegistrationService changeRegistrationService, final UpdateErrorRegistrationService updateErrorRegistrationService, final Transactions transactions) {
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

        guard(notNull(changeRegistrationService));
        this.changeRegistrationService = changeRegistrationService;

        guard(notNull(updateErrorRegistrationService));
        this.updateErrorRegistrationService = updateErrorRegistrationService;

        guard(notNull(changeRepository));
        this.changeRepository = changeRepository;

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
                FeedsService.removeFeedComponents(header.id, this.feedUpdateTaskRepository, this.feedHeadersRepository, this.feedItemsRepository, this.readFeedItemsRepository, this.changeRegistrationService, this.updateErrorRegistrationService);
            }

            this.feedUpdateTaskSchedulerContextRepository.clear();

            this.categoriesRepository.clear();

            this.importJobContextRepository.clear();

            this.changeRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }


}
