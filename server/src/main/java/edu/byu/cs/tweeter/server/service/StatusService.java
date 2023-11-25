package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.StoriesTable;
import edu.byu.cs.tweeter.server.dao.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowsDAO;
import edu.byu.cs.tweeter.server.dao.IStatusesDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;

public class StatusService {
    private final IUserDAO userDAO;
    private final IFollowsDAO followsDAO;
    private final IStatusesDAO statusesDAO;
    public StatusService(IDAOFactory factory) {
        userDAO = factory.getUserDAO();
        followsDAO = factory.getFollowsDAO();
        statusesDAO = factory.getStatusDAO();
    }

    /**
     * Returns the statuses requested.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses and hasMorePages.
     */
    public StatusesResponse getFeed(StatusesRequest request) {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        DataPage<FeedTable> result = statusesDAO.getFeed(request.getTargetAlias(), request.getLastStatus(), request.getLimit());
        List<Status> statuses = getStatusesFromFeedQueryResult(result.getValues());
        return new StatusesResponse(statuses, result.isHasMorePages());
    }

    public StatusesResponse getStories(StatusesRequest request) {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        DataPage<StoriesTable> result = statusesDAO.getStories(request.getTargetAlias(), request.getLastStatus(), request.getLimit());
        List<Status> statuses = getStatusesFromStoryQueryResult(result.getValues());
        return new StatusesResponse(statuses, result.isHasMorePages());
    }

    private List<Status> getStatusesFromStoryQueryResult(List<StoriesTable> entries) {
        List<Status> statuses = new ArrayList<>();
        for (StoriesTable entry : entries) {
            User user = userDAO.getUser(entry.getUserAlias());
            statuses.add(new Status(entry.getPost(), user, entry.getTimestamp(), entry.getUrls(), entry.getMentions()));
        }
        return statuses;
    }

    private List<Status> getStatusesFromFeedQueryResult(List<FeedTable> entries) {
        List<Status> statuses = new ArrayList<>();
        for (FeedTable entry : entries) {
            User user = userDAO.getUser(entry.getAuthorAlias());
            statuses.add(new Status(entry.getPost(), user, entry.getTimestamp(), entry.getUrls(), entry.getMentions()));
        }
        return statuses;
    }

    public Response postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
        int followerCount = userDAO.getFollowerCount(request.getStatus().getUser().getAlias());
        if (followerCount > 0) {
            DataPage<FollowsTable> result = followsDAO.getFollowers(request.getStatus().getUser().getAlias(), followerCount, null);
            List<String> followerAliases = FollowService.getFollowerAliases(result);
            statusesDAO.postStatus(request.getStatus(), followerAliases);
        }
        else statusesDAO.postStatus(request.getStatus(), null);
        return new Response(true);
    }
}
