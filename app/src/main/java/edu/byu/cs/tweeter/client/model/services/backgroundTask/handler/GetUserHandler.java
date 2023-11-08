package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter.GetUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BackgroundTaskHandler<GetUserObserver> {
    public GetUserHandler(GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GetUserObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.getUserSucceeded(user);
    }
}
