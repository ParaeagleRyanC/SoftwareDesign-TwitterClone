package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class UnfollowHandler extends BackgroundTaskHandler<MainPresenter.FollowUnfollowObserver> {
    public UnfollowHandler(MainPresenter.FollowUnfollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.FollowUnfollowObserver observer, Bundle data) {
        observer.followUnfollowSucceeded(true);
    }
}
