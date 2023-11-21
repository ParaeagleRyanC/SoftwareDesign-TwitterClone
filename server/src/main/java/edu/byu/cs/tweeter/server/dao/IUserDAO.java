package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

public interface IUserDAO {
    User login(String username, String password);
    User register(String username, String password, String firstname, String lastname, String imageUrl);
    User getUser(String alias);
    void deleteUser(User user);
}
