package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;

public interface IFollowsDAO {
    DataPage<FollowsTable> getFollowees(String followerAlias, int pageSize, String lastFolloweeAlias);
    DataPage<FollowsTable> getFollowers(String followeeAlias, int pageSize, String lastFollowerAlias);
    boolean isFollower(String followerAlias, String followeeAlias);
    void follow(String followerAlias, String followeeAlias);
    void unfollow(String followerAlias, String followeeAlias);
}
