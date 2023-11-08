package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowUnfollowRequest {

    private String targetAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowUnfollowRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetAlias the user to follow.
     */
    public FollowUnfollowRequest(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }
}