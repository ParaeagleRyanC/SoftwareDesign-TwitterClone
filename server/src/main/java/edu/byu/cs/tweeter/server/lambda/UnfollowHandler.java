package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that follows or unfollows a user.
 */
public class UnfollowHandler implements RequestHandler<FollowUnfollowRequest, Response> {

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
    public Response handleRequest(FollowUnfollowRequest request, Context context) {
        FollowService service = new FollowService();
        return service.unfollow(request);
    }
}
