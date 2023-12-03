package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.DynamoDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.lambda.util.UpdateFeed;

public class UpdateFeedQueueProcessor implements RequestHandler<SQSEvent, Void> {
    private final String FeedTableName = "feed";
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        UpdateFeed updateFeed;
        ObjectMapper mapper = new ObjectMapper();
        DynamoDAO dynamoDAO = new DynamoDAO();
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

            dynamoDAO.addItemsBatch(feedEntries, FeedTableName, FeedTable.class);
        }
        return null;
    }
}
