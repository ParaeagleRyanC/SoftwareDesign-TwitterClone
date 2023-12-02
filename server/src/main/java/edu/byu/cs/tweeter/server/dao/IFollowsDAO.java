package edu.byu.cs.tweeter.server.dao;

public interface IFollowsDAO<T> {
    DataPage<T> getFollowees(String followerAlias, int pageSize, String lastFolloweeAlias);
    DataPage<T> getFollowers(String followeeAlias, int pageSize, String lastFollowerAlias);
    boolean isFollower(String followerAlias, String followeeAlias);
    void follow(String followerAlias, String followeeAlias);
    void unfollow(String followerAlias, String followeeAlias);
}
