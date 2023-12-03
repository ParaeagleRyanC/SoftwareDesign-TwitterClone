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

public class StatusService extends BaseService {
    public StatusService(IDAOFactory factory) {
        super(factory);
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
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new StatusesResponse("Token has expired");
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
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new StatusesResponse("Token has expired");
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
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken())) return new Response(false, "Token has expired");
        statusesDAO.postStatus(request.getStatus());
        return new Response(true);
    }
}
