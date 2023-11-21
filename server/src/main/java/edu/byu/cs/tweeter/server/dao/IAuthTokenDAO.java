package edu.byu.cs.tweeter.server.dao;

public interface IAuthTokenDAO {
    void addToken(String token, String timeStamp);
    String validateToken(String token);
    void deleteToken(String token);
}
