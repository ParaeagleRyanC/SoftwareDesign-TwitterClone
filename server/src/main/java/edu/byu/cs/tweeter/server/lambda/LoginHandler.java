package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, AuthenticatedResponse> {
    @Override
    public AuthenticatedResponse handleRequest(LoginRequest loginRequest, Context context) {
        UserService userService = new UserService(new DynamoDbDAOFactory());
        return userService.login(loginRequest);
    }
}
