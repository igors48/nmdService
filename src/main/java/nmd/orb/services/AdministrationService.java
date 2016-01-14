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
public class AdministrationService {

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
    private final Cache cache;
    private final Transactions transactions;

    public AdministrationService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final CategoriesRepository categoriesRepository, final ImportJobContextRepository importJobContextRepository, final ChangeRepository changeRepository, final ChangeRegistrationService changeRegistrationService, final UpdateErrorRegistrationService updateErrorRegistrationService, final Cache cache, final Transactions transactions) {
        guard(notNull(this.feedHeadersRepository = feedHeadersRepository));
        guard(notNull(this.feedItemsRepository = feedItemsRepository));
        guard(notNull(this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository));
        guard(notNull(this.feedUpdateTaskRepository = feedUpdateTaskRepository));
        guard(notNull(this.readFeedItemsRepository = readFeedItemsRepository));
        guard(notNull(this.categoriesRepository = categoriesRepository));
        guard(notNull(this.importJobContextRepository = importJobContextRepository));
        guard(notNull(this.changeRegistrationService = changeRegistrationService));
        guard(notNull(this.updateErrorRegistrationService = updateErrorRegistrationService));
        guard(notNull(this.changeRepository = changeRepository));
        guard(notNull(this.cache = cache));
        guard(notNull(this.transactions = transactions));
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

    public void flushCache() {
        this.cache.flush();
    }

}
