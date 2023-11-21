package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users' followers.
 */
public class GetFolloweesHandler implements RequestHandler<FollowsRequest, FollowsResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers.
     */
    @Override
    public FollowsResponse handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService(new DynamoDbDAOFactory());
        return service.getFollowees(request);
    }
}
