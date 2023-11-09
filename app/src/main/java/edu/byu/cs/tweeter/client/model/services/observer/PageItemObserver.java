package edu.byu.cs.tweeter.client.model.services.observer;

import java.util.List;

public interface PageItemObserver<T> extends ServiceObserver {
    void getItemsSucceeded(List<T> items, boolean hasMorePages);
}
