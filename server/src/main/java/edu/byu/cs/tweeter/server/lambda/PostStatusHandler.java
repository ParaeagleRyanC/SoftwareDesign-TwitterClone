package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, Response> {

//    private final String getFollowersQueueUrl = "https://sqs.us-west-2.amazonaws.com/144669027494/get_followers_queue";

    @Override
    public Response handleRequest(PostStatusRequest request, Context context) {
//        ObjectMapper mapper = new ObjectMapper();
//        String messageBody;
//        try {
//            messageBody = mapper.writeValueAsString(request.getStatus());
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        SendMessageRequest send_msg_request = new SendMessageRequest()
//                .withQueueUrl(getFollowersQueueUrl)
//                .withMessageBody(messageBody);
//        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
//        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
//        System.out.println("Message ID: " + send_msg_result.getMessageId());
        // lines 23 - 35 should go into status service

        StatusService statusService = new StatusService(new DynamoDbDAOFactory());
        return statusService.postStatus(request);
    }
}
