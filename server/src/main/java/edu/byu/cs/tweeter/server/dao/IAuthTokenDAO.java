package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDAO {
    AuthToken addToken();
    boolean validateToken(String token);
    void deleteToken(String token);
}
