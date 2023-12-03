package edu.byu.cs.tweeter.server.lambda.util;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeed {
    private List<String> followersAliases;
    private Status status;
    private UpdateFeed() {}
    public UpdateFeed(List<String> followersAliases, Status status) {
        this.followersAliases = followersAliases;
        this.status = status;
    }

    public List<String> getFollowersAliases() {
        return followersAliases;
    }

    public Status getStatus() {
        return status;
    }
}
