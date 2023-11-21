package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;

public interface IStatusesDAO {
    StatusesResponse getStory(StatusesRequest request);
    Response postStatus(PostStatusRequest request);
    void deleteStatus(User user, long timestamp);
}
