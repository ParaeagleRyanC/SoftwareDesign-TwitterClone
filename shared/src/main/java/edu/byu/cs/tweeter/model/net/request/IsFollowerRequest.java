package edu.byu.cs.tweeter.model.net.request;

public class IsFollowerRequest {
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest() {}

    public IsFollowerRequest(String followerAlias, String followeeAlias) {
        this.followeeAlias = followeeAlias;
        this.followerAlias = followerAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
