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
    private final AdministrationServiceWrapper administrationServiceWrapper = new AdministrationServiceWrapperImpl(GaeServices.INSTANCE.getAdministrationService());
    private final UpdatesServiceWrapper updatesServiceWrapper = new UpdatesServiceWrapperImpl(GaeServices.INSTANCE.getUpdatesService());
    private final ContentFilterServiceWrapper contentFilterServiceWrapper = new ContentFilterServiceWrapperImpl(GaeServices.INSTANCE.getContentFilterService());

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

    public AdministrationServiceWrapper getAdministrationServiceWrapper() {
        return this.administrationServiceWrapper;
    }

    public UpdatesServiceWrapper getUpdatesServiceWrapper() {
        return this.updatesServiceWrapper;
    }

    public ContentFilterServiceWrapper getContentFilterServiceWrapper() {
        return this.contentFilterServiceWrapper;
    }
}
