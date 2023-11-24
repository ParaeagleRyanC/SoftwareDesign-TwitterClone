package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowsCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowsCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowsDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private IAuthTokenDAO authTokenDAO;
    private IUserDAO userDAO;
    private IFollowsDAO followsDAO;
    public FollowService(IDAOFactory factory) {
        authTokenDAO = factory.getAuthDAO();
        userDAO = factory.getUserDAO();
        followsDAO = factory.getFollowsDAO();
    }

    public FollowsResponse getFollowees(FollowsRequest request) {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new FollowsResponse("Token has expired.");
        DataPage<FollowsTable> result = followsDAO.getFollowees(request.getTargetAlias(), request.getLimit(), request.getLastPersonAlias());
        List<String> aliases =getFolloweeAliases(result);
        List<User> users = userDAO.getUsers(aliases);
        return new FollowsResponse(users, result.isHasMorePages());
    }

    private List<String> getFolloweeAliases(DataPage<FollowsTable> data) {
        List<String> aliases = new ArrayList<>();
        for (FollowsTable item : data.getValues()) {
            if (item != null) aliases.add(item.getFolloweeAlias());
        }
        return aliases;
    }

    private List<String> getFollowerAliases(DataPage<FollowsTable> data) {
        List<String> aliases = new ArrayList<>();
        for (FollowsTable item : data.getValues()) {
            if (item != null) aliases.add(item.getFollowerAlias());
        }
        return aliases;
    }

    public FollowsResponse getFollowers(FollowsRequest request) {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new FollowsResponse("Token has expired.");
        DataPage<FollowsTable> result = followsDAO.getFollowers(request.getTargetAlias(), request.getLimit(), request.getLastPersonAlias());
        List<String> aliases =getFollowerAliases(result);
        List<User> users = userDAO.getUsers(aliases);
        return new FollowsResponse(users, result.isHasMorePages());
    }

    public Response follow(FollowUnfollowRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        } else if (request.getCurrentUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a current user alias");
        }
        userDAO.updateFollowingCount(request.getCurrentUserAlias(), 1);
        userDAO.updateFollowerCount(request.getTargetAlias(), 1);
        followsDAO.follow(request.getCurrentUserAlias(), request.getTargetAlias());
        return new Response(true);
    }

    public Response unfollow(FollowUnfollowRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        } else if (request.getCurrentUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a current user alias");
        }
        userDAO.updateFollowingCount(request.getCurrentUserAlias(), -1);
        userDAO.updateFollowerCount(request.getTargetAlias(), -1);
        followsDAO.unfollow(request.getCurrentUserAlias(), request.getTargetAlias());
        return new Response(true);
    }

    public GetFollowsCountResponse getFollowingCount(GetFollowsCountRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        int count = userDAO.getFollowingCount(request.getTargetAlias());
        return new GetFollowsCountResponse(true, count);
    }

    public GetFollowsCountResponse getFollowerCount(GetFollowsCountRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        int count = userDAO.getFollowerCount(request.getTargetAlias());
        return new GetFollowsCountResponse(true, count);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        boolean isFollow = followsDAO.isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
        return new IsFollowerResponse(true, isFollow);
    }
}
