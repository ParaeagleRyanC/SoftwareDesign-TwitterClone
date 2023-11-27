package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetFollowsCountRequest {
    private String targetAlias;
    private String authToken;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowsCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetAlias userAlias to retrieve follower/following count for
     */
    public GetFollowsCountRequest(String targetAlias, String authToken) {
        this.targetAlias = targetAlias;
        this.authToken = authToken;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
