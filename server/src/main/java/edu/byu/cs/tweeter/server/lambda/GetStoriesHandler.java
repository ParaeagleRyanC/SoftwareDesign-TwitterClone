package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoriesHandler implements RequestHandler<StatusesRequest, StatusesResponse>  {
    @Override
    public StatusesResponse handleRequest(StatusesRequest request, Context context) {
        StatusService service = new StatusService(new DynamoDbDAOFactory());
        return service.getStories(request);
    }
}
