package edu.byu.cs.tweeter.model.net.request;

public class FollowUnfollowRequest {

    private String currentUserAlias;

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
    public FollowUnfollowRequest(String targetAlias, String currentUserAlias) {
        this.targetAlias = targetAlias;
        this.currentUserAlias = currentUserAlias;
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
}
