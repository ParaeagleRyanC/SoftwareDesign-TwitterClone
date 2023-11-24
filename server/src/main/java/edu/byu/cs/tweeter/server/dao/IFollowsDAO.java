package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;

public interface IFollowsDAO {
    DataPage<FollowsTable> getFollowees(String followerAlias, int pageSize, String lastFolloweeAlias);
    DataPage<FollowsTable> getFollowers(String followeeAlias, int pageSize, String lastFollowerAlias);
    boolean isFollower(String followerAlias, String followeeAlias);
    void follow(String followerAlias, String followeeAlias);
    void unfollow(String followerAlias, String followeeAlias);
}
