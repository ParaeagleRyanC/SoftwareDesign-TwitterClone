package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

// GetFollowingCountHandler
public class GetFollowingCountHandler extends BackgroundTaskHandler<MainPresenter.GetFollowCountObserver> {
    public GetFollowingCountHandler(MainPresenter.GetFollowCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.GetFollowCountObserver observer, Bundle data) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.getFollowCountSucceed(count, false);
    }
}
