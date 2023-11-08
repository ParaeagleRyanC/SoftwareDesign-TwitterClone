package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class LogoutHandler extends BackgroundTaskHandler<MainPresenter.LogoutObserver> {
    public LogoutHandler(MainPresenter.LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(MainPresenter.LogoutObserver observer, Bundle data) {
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        observer.logoutSucceeded();
    }
}
