package edu.byu.cs.tweeter.client.view;

import java.util.List;

public interface PageView extends AllButMainView {
    void startLoading();
    void endLoading();
    <T> void addItems(List<T> items);
}
