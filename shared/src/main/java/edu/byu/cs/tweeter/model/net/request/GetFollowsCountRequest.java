package edu.byu.cs.tweeter.model.net.request;

public class GetFollowsCountRequest {
    private String targetAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowsCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetAlias userAlias to retrieve follower/following count for
     */
    public GetFollowsCountRequest(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }
}
