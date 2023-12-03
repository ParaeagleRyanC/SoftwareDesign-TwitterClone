package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.DynamoFollowsDAO;
import edu.byu.cs.tweeter.server.lambda.util.UpdateFeed;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersQueueProcessor implements RequestHandler<SQSEvent, Void> {
    private final int batchSize = 25;
    private final String updateFeedQueueUrl = "https://sqs.us-west-2.amazonaws.com/144669027494/update_feed_queue";
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        Status status;
        ObjectMapper mapper = new ObjectMapper();
        DynamoFollowsDAO followsDAO = new DynamoFollowsDAO();
        String lastFollowerAlias = null;
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
            try {
                status = mapper.readValue(msg.getBody(), Status.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                DataPage<FollowsTable> result = followsDAO.getFollowers(status.getUser().getAlias(), batchSize, lastFollowerAlias);
                List<String> followersAliases = FollowService.getFollowerAliases(result);
                String messageBody;
                try {
                    messageBody = mapper.writeValueAsString(new UpdateFeed(followersAliases, status));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(messageBody);
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(updateFeedQueueUrl)
                        .withMessageBody(messageBody);
                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
                System.out.println("Message ID: " + send_msg_result.getMessageId());
                if (!result.isHasMorePages()) break;
                lastFollowerAlias = followersAliases.get(batchSize-1);
            }
        }
        return null;
    }
}
