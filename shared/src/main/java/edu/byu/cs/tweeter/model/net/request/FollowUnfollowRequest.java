package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowUnfollowRequest {

    private String currentUserAlias;

    private String targetAlias;

    private AuthToken authToken;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowUnfollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetAlias the user to follow.
     */
    public FollowUnfollowRequest(String targetAlias, String currentUserAlias, AuthToken authToken) {
        this.targetAlias = targetAlias;
        this.currentUserAlias = currentUserAlias;
        this.authToken = authToken;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public String getCurrentUserAlias() {
        return currentUserAlias;
    }

    public void setCurrentUserAlias(String currentUserAlias) {
        this.currentUserAlias = currentUserAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
