package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public interface IFollowsDAO {
    FollowsResponse getFollowing(FollowsRequest request);
    FollowsResponse getFollower(FollowsRequest request);
    IsFollowerResponse isFollower(IsFollowerRequest request);
    Response follow(FollowUnfollowRequest request);
    Response unfollow(FollowUnfollowRequest request);
}
