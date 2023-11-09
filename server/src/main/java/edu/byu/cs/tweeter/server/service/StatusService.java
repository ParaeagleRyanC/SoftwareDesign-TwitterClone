package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

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

        Pair<List<Status>, Boolean> pair = getStatusDAO().getStatuses(request.getLastStatus(), request.getLimit());
        return new StatusesResponse(pair.getFirst(), pair.getSecond());
    }

    public StatusesResponse getStories(StatusesRequest request) {
        if(request.getTargetAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<Status>, Boolean> pair = getStatusDAO().getStatuses(request.getLastStatus(), request.getLimit());
        return new StatusesResponse(pair.getFirst(), pair.getSecond());
    }

    public Response postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
        return new Response(true);
    }

    StatusDAO getStatusDAO() { return new StatusDAO(); }
}
