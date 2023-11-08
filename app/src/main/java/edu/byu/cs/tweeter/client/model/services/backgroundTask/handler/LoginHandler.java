package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginHandler extends BackgroundTaskHandler<LoginPresenter.LoginObserver> {
    public LoginHandler(LoginPresenter.LoginObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LoginPresenter.LoginObserver observer, Bundle data) {
        User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

        // Cache user session information
        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.loginRegisterSucceeded(loggedInUser);
    }
}
