package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService extends GeneralService {

    public static final String LOGIN_URL_PATH = "/login";

    public void login(String alias, String password, LoginPresenter.LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias,
                         String password, String imageBytesBase64, RegisterPresenter.RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(observer));
        execute(registerTask);
    }
}
