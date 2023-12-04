package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.DynamoDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDbDAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.lambda.util.UpdateFeed;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeedQueueProcessor implements RequestHandler<SQSEvent, Void> {
//    private final String FeedTableName = "feed";
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        UpdateFeed updateFeed;
        ObjectMapper mapper = new ObjectMapper();
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
            try {
                updateFeed = mapper.readValue(msg.getBody(), UpdateFeed.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            List<FeedTable> feedEntries = new ArrayList<>();
            for (String alias : updateFeed.getFollowersAliases()) {
                FeedTable feedEntry = new FeedTable();
                feedEntry.setAuthorAlias(updateFeed.getStatus().getUser().getAlias());
                feedEntry.setPost(updateFeed.getStatus().getPost());
                feedEntry.setTimestamp(updateFeed.getStatus().getTimestamp());
                feedEntry.setUrls(updateFeed.getStatus().getUrls());
                feedEntry.setMentions(updateFeed.getStatus().getMentions());
                feedEntry.setBelongToAlias(alias);
                feedEntries.add(feedEntry);
            }
            // lines below should be a call to status service to invoke this method in the service
            // DAO should not be accessed here
//            DynamoDAO dynamoDAO = new DynamoDAO();
//            dynamoDAO.addItemsBatch(feedEntries, FeedTableName, FeedTable.class);
            StatusService statusService = new StatusService(new DynamoDbDAOFactory());
            statusService.addFeedInBatch(feedEntries);
        }
        return null;
    }
}
