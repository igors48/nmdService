package nmd.orb.gae;

import nmd.orb.http.wrappers.*;

/**
 * @author : igu
 */
public enum GaeWrappers {

    INSTANCE;

    private final CategoriesServiceWrapper categoriesServiceWrapper = new CategoriesServiceWrapperImpl(GaeServices.INSTANCE.getCategoriesService());
    private final CronServiceWrapper cronServiceWrapper = new CronServiceWrapperImpl(GaeServices.INSTANCE.getCronService());
    private final FeedsServiceWrapper feedsServiceWrapper = new FeedsServiceWrapperImpl(GaeServices.INSTANCE.getFeedsService());
    private final ImportServiceWrapper importServiceWrapper = new ImportServiceWrapperImpl(GaeServices.INSTANCE.getImportService());
    private final ReadsServiceWrapper readsServiceWrapper = new ReadsServiceWrapperImpl(GaeServices.INSTANCE.getReadsService());
    private final ResetServiceWrapper resetServiceWrapper = new ResetServiceWrapperImpl(GaeServices.INSTANCE.getResetService());
    private final UpdatesServiceWrapper updatesServiceWrapper = new UpdatesServiceWrapperImpl(GaeServices.INSTANCE.getUpdatesService());

    public CategoriesServiceWrapper getCategoriesServiceWrapper() {
        return this.categoriesServiceWrapper;
    }

    public CronServiceWrapper getCronServiceWrapper() {
        return this.cronServiceWrapper;
    }

    public FeedsServiceWrapper getFeedsServiceWrapper() {
        return this.feedsServiceWrapper;
    }

    public ImportServiceWrapper getImportServiceWrapper() {
        return this.importServiceWrapper;
    }

    public ReadsServiceWrapper getReadsServiceWrapper() {
        return this.readsServiceWrapper;
    }

    public ResetServiceWrapper getResetServiceWrapper() {
        return this.resetServiceWrapper;
    }

    public UpdatesServiceWrapper getUpdatesServiceWrapper() {
        return this.updatesServiceWrapper;
    }

}
