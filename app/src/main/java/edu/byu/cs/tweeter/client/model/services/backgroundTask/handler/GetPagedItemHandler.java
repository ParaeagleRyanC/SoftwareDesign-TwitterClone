package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.services.observer.PageItemObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;

public class GetPagedItemHandler<T> extends BackgroundTaskHandler<PageItemObserver> {

    private final static String ITEMS_KEY = "items";
    private final static String MORE_PAGES_KEY = "more-pages";
    public GetPagedItemHandler(PageItemObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(PageItemObserver observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(MORE_PAGES_KEY);
        // T lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        observer.getItemsSucceeded(items, hasMorePages);
    }
}
