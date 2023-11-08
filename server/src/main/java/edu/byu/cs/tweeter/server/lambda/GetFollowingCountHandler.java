package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowsCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowsCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingCountHandler implements RequestHandler<GetFollowsCountRequest, GetFollowsCountResponse> {
    @Override
    public GetFollowsCountResponse handleRequest(GetFollowsCountRequest request, Context context) {
        FollowService service = new FollowService();
        return service.getFollowingCount(request);
    }
}
