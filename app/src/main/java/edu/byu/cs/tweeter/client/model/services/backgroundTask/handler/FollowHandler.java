package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class FollowHandler extends BackgroundTaskHandler<MainPresenter.FollowUnfollowObserver> {
    public FollowHandler(MainPresenter.FollowUnfollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.FollowUnfollowObserver observer, Bundle data) {
        observer.followUnfollowSucceeded(false);
    }
}
