package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public class DynamoFollowsDAO implements IFollowsDAO {
    @Override
    public FollowsResponse getFollowing(FollowsRequest request) {
        return null;
    }

    @Override
    public FollowsResponse getFollower(FollowsRequest request) {
        return null;
    }

    @Override
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        return null;
    }

    @Override
    public Response follow(FollowUnfollowRequest request) {
        return null;
    }

    @Override
    public Response unfollow(FollowUnfollowRequest request) {
        return null;
    }
}
