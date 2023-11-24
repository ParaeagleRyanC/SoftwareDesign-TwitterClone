package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

// GetFollowersCountHandler
public class GetFollowersCountHandler extends BackgroundTaskHandler<MainPresenter.GetFollowCountObserver> {

    public GetFollowersCountHandler(MainPresenter.GetFollowCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.GetFollowCountObserver observer, Bundle data) {
        int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
        observer.getFollowCountSucceed(count, false);
    }
}
