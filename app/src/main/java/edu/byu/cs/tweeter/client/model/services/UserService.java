package edu.byu.cs.tweeter.client.model.services;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService extends GeneralService {

    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String LOGOUT_URL_PATH = "/logout";
    public static final String GET_USER_URL_PATH = "/getuser";

    public void login(String alias, String password, LoginPresenter.LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        execute(loginTask);
    }

    public void logout(AuthToken authToken, MainPresenter.LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));
        execute(logoutTask);
    }

    public void register(String firstName, String lastName, String alias,
                         String password, String imageBytesBase64, RegisterPresenter.RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(observer));
        execute(registerTask);
    }

    public void getUser(AuthToken authToken, String alias, PagedPresenter.GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        execute(getUserTask);
    }
}
