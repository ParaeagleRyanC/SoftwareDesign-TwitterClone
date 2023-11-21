package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowsCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowsCountResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowerCountHandler implements RequestHandler<GetFollowsCountRequest, GetFollowsCountResponse> {
    @Override
    public GetFollowsCountResponse handleRequest(GetFollowsCountRequest request, Context context) {
        FollowService service = new FollowService(new DynamoDbDAOFactory());
        return service.getFollowerCount(request);
    }
}
