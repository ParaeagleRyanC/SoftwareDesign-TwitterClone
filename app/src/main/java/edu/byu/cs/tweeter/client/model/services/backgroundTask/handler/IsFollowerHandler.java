package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class IsFollowerHandler extends BackgroundTaskHandler<MainPresenter.IsFollowerObserver> {
    public IsFollowerHandler(MainPresenter.IsFollowerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.IsFollowerObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        // If logged in user is a follower of the selected user, display the follow button as "following"
        observer.isFollowSucceeded(isFollower);
    }
}
