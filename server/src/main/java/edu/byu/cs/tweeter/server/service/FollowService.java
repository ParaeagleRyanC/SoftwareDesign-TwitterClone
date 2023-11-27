package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

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
import edu.byu.cs.tweeter.server.dao.IDAOFactory;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {
    public FollowService(IDAOFactory factory) {
        super(factory);
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

    public static List<String> getFollowerAliases(DataPage<FollowsTable> data) {
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
        List<String> aliases = getFollowerAliases(result);
        List<User> users = userDAO.getUsers(aliases);
        return new FollowsResponse(users, result.isHasMorePages());
    }

    public Response follow(FollowUnfollowRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        } else if (request.getCurrentUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a current user alias");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new Response(false, "Token has expired");
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
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new Response(false, "Token has expired");
        userDAO.updateFollowingCount(request.getCurrentUserAlias(), -1);
        userDAO.updateFollowerCount(request.getTargetAlias(), -1);
        followsDAO.unfollow(request.getCurrentUserAlias(), request.getTargetAlias());
        return new Response(true);
    }

    public GetFollowsCountResponse getFollowingCount(GetFollowsCountRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken())) return new GetFollowsCountResponse("Token has expired");
        int count = userDAO.getFollowingCount(request.getTargetAlias());
        return new GetFollowsCountResponse(true, count);
    }

    public GetFollowsCountResponse getFollowerCount(GetFollowsCountRequest request) {
        if (request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken())) return new GetFollowsCountResponse("Token has expired");
        int count = userDAO.getFollowerCount(request.getTargetAlias());
        return new GetFollowsCountResponse(true, count);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new IsFollowerResponse("Token has expired.");
        boolean isFollow = followsDAO.isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
        return new IsFollowerResponse(true, isFollow);
    }
}
