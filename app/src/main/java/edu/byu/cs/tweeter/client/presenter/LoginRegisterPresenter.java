package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.view.LoginRegisterView;

public abstract class LoginRegisterPresenter extends BasePresenter<LoginRegisterView> {
    public LoginRegisterPresenter(LoginRegisterView view) {
        super(view);
    }

    public boolean validateAliasAndPassword(String alias, String password) {
        if (alias.length() == 0) {
            view.showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }
        return true;
    }
}
