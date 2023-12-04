package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.DynamoDAO;
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

        // send queue message here
        final String getFollowersQueueUrl = "https://sqs.us-west-2.amazonaws.com/144669027494/get_followers_queue";
        ObjectMapper mapper = new ObjectMapper();
        String messageBody;
        try {
            messageBody = mapper.writeValueAsString(request.getStatus());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(getFollowersQueueUrl)
                .withMessageBody(messageBody);
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
        System.out.println("Message ID: " + send_msg_result.getMessageId());
        return new Response(true);
    }

    public void addFeedInBatch(List<FeedTable> feedEntries) {
        final String FeedTableName = "feed";
        DynamoDAO dynamoDAO = new DynamoDAO();
        dynamoDAO.addItemsBatch(feedEntries, FeedTableName, FeedTable.class);
    }
}
