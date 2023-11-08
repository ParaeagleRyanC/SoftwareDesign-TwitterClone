package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class PostStatusHandler extends BackgroundTaskHandler<MainPresenter.PostStatusObserver> {
    public PostStatusHandler(MainPresenter.PostStatusObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.PostStatusObserver observer, Bundle data) {
        observer.postStatusSucceeded("Successfully Posted!");
    }
}
