package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {
    private IUserDAO userDAO;
    private IAuthTokenDAO authTokenDAO;
    public UserService(IDAOFactory factory) {
        userDAO = factory.getUserDAO();
        authTokenDAO = factory.getAuthDAO();
    }

    public AuthenticatedResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = userDAO.login(request.getUsername(), request.getPassword());
        if (user == null) return new AuthenticatedResponse("Bad login. Try again.");
        AuthToken authToken = authTokenDAO.addToken();
        return new AuthenticatedResponse(user, authToken);
    }

    public Response logout(LogoutRequest request) {
        authTokenDAO.deleteToken(request.getAuthToken());
        return new Response(true);
    }

    public AuthenticatedResponse register(RegisterRequest request) {
        if (request.getAlias() == null){
            throw new RuntimeException("[Bad Request] Missing an alias");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = userDAO.register(request.getAlias(), request.getPassword(), request.getFirstName(), request.getLastName(), request.getImage());
        if (user == null) {
            return new AuthenticatedResponse("User already exists. Login instead.");
        }
        AuthToken authToken = authTokenDAO.addToken();
        return new AuthenticatedResponse(user, authToken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing an alias");
        }
        // TODO: Generates dummy data. Replace with a real implementation.
        User user = userDAO.getUser(request.getAlias());
        if (user != null)
            return new GetUserResponse(user);
        return new GetUserResponse(getFakeData().findUserByAlias(request.getAlias())); // remove this when done
    }

//    /**
//     * Returns the dummy user to be returned by the login operation.
//     * This is written as a separate method to allow mocking of the dummy user.
//     *
//     * @return a dummy user.
//     */
//    User getDummyUser() {
//        return getFakeData().getFirstUser();
//    }

//    /**
//     * Returns the dummy auth token to be returned by the login operation.
//     * This is written as a separate method to allow mocking of the dummy auth token.
//     *
//     * @return a dummy auth token.
//     */
//    AuthToken getDummyAuthToken() {
//        return getFakeData().getAuthToken();
//    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
