package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;
import edu.byu.cs.tweeter.client.view.LoginRegisterView;

public class RegisterPresenter extends LoginRegisterPresenter {

    public static class RegisterObserver extends LoginRegisterObserver {
        public RegisterObserver(LoginRegisterView view) {
            super(view);
        }
        @Override
        public String getExceptionMessage() {
            return "Failed to register because of exception: ";
        }
    }

    public RegisterPresenter(LoginRegisterView view) {
        super(view);
    }

    public void register(String firstName, String lastName,
                         String alias, String password, String imageBase64) {
        if (validateRegistration(firstName, lastName, imageBase64) && validateAliasAndPassword(alias, password)) {
            view.hideErrorMessage();
            view.showErrorMessage("Registering...");
            var userService = new UserService();
            userService.register(firstName, lastName, alias, password, imageBase64,new RegisterObserver(view));
        }
    }

    public boolean validateRegistration(String firstName, String lastName, String imageToUpload) {
        if (firstName.length() == 0) {
            view.showErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.showErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (imageToUpload == null) {
            view.showErrorMessage("Profile image must be uploaded.");
            return false;
        }
        return true;
    }

}
