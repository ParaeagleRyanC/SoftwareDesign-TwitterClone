package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;

public class DynamoStatusesDAO implements IStatusesDAO {
    @Override
    public StatusesResponse getStory(StatusesRequest request) {
        return null;
    }

    @Override
    public Response postStatus(PostStatusRequest request) {
        return null;
    }

    @Override
    public void deleteStatus(User user, long timestamp) {

    }
}
