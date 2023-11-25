package edu.byu.cs.tweeter.server.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.IDAOFactory;

public class UserService extends BaseService {
    public UserService(IDAOFactory factory) {
        super(factory);
    }

    public AuthenticatedResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

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

        User user = userDAO.register(request.getAlias(), encryptPassword(request.getPassword()), request.getFirstName(), request.getLastName(), request.getImage());
        if (user == null) {
            return new AuthenticatedResponse("User already exists. Login instead.");
        }
        AuthToken authToken = authTokenDAO.addToken();
        return new AuthenticatedResponse(user, authToken);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing an alias");
        }
        User user = userDAO.getUser(request.getAlias());
        return new GetUserResponse(user);
    }
}
