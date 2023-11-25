package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface IUserDAO {
    User login(String username, String password);
    User register(String username, String password, String firstname, String lastname, String imageUrl);
    User getUser(String alias);
    void deleteUser(User user);
    int getFollowerCount(String alias);
    int getFollowingCount(String alias);
    void updateFollowingCount(String alias, int count);
    void updateFollowerCount(String alias, int count);
    List<User> getUsers(List<String> aliases);
}
