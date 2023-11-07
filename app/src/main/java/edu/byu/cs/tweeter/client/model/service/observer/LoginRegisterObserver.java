package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.view.LoginRegisterView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class LoginRegisterObserver implements ServiceObserver {

    protected LoginRegisterView view;

    public LoginRegisterObserver(LoginRegisterView view) { this.view = view; }

    public void loginRegisterSucceeded(User user) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello " + user.getName());
        view.openMainView(user);
    }

    @Override
    public void handleFailure(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void handleException(Exception exception) {
        view.showErrorMessage(getExceptionMessage() + exception.getMessage());
    }

    public abstract String getExceptionMessage();
}