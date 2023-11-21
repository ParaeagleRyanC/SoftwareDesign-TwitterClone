package edu.byu.cs.tweeter.server.dao;

public class DynamoAuthDAO implements IAuthTokenDAO {
    @Override
    public void addToken(String token, String timeStamp) {

    }

    @Override
    public String validateToken(String token) {
        return null;
    }

    @Override
    public void deleteToken(String token) {

    }
}
