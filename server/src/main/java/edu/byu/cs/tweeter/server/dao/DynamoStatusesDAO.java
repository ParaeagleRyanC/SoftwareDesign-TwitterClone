package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.StoriesTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoStatusesDAO implements IStatusesDAO {
    private static final String StoriesTableName = "stories";
    private static final String FeedTableName = "feed";
    private static final String AttrTimestamp = "timestamp";
    private static final String AttrUserAlias = "userAlias";
    // DynamoDB client
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    @Override
    public DataPage<StoriesTable> getStories(String userAlias, Status lastStatus, int limit) {
        DynamoDbTable<StoriesTable> table = enhancedClient.table(StoriesTableName, TableSchema.fromBean(StoriesTable.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastStatus != null) {
            if(isNonEmptyString(lastStatus.getPost())) {
                // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(AttrUserAlias, AttributeValue.builder().s(userAlias).build());
                startKey.put(AttrTimestamp, AttributeValue.builder().s(lastStatus.getTimestamp().toString()).build());

                requestBuilder.exclusiveStartKey(startKey);
            }
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<StoriesTable> result = new DataPage<>();

        PageIterable<StoriesTable> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<StoriesTable> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> result.getValues().add(status));
                });

        return result;
    }

    @Override
    public DataPage<FeedTable> getFeed(String userAlias, Status lastStatus, int limit) {
        DynamoDbTable<FeedTable> table = enhancedClient.table(FeedTableName, TableSchema.fromBean(FeedTable.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastStatus != null) {
            if(isNonEmptyString(lastStatus.getPost())) {
                // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(AttrUserAlias, AttributeValue.builder().s(userAlias).build());
                startKey.put(AttrTimestamp, AttributeValue.builder().s(lastStatus.getTimestamp().toString()).build());

                requestBuilder.exclusiveStartKey(startKey);
            }
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FeedTable> result = new DataPage<>();

        PageIterable<FeedTable> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedTable> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> result.getValues().add(status));
                });

        return result;
    }

    @Override
    public void postStatus(Status status, List<String> followerAliases) {
        DynamoDbTable<StoriesTable> storiesTable = enhancedClient.table(StoriesTableName, TableSchema.fromBean(StoriesTable.class));
        StoriesTable newStoryEntry = new StoriesTable();
        newStoryEntry.setPost(status.getPost());
        newStoryEntry.setTimestamp(status.getTimestamp());
        newStoryEntry.setUserAlias(status.getUser().getAlias());
        newStoryEntry.setUrls(status.getUrls());
        newStoryEntry.setMentions(status.getMentions());
        storiesTable.putItem(newStoryEntry);

        if (followerAliases != null) {
            DynamoDbTable<FeedTable> feedTable = enhancedClient.table(FeedTableName, TableSchema.fromBean(FeedTable.class));
            FeedTable newFeedEntry = new FeedTable();
            newFeedEntry.setPost(status.getPost());
            newFeedEntry.setTimestamp(status.getTimestamp());
            newFeedEntry.setAuthorAlias(status.getUser().getAlias());
            newFeedEntry.setUrls(status.getUrls());
            newFeedEntry.setMentions(status.getMentions());
            for (String alias: followerAliases) {
                newFeedEntry.setBelongToAlias(alias);
                feedTable.putItem(newFeedEntry);
            }
        }
    }

    @Override
    public void deleteStatus(User user, long timestamp) {

    }
}
