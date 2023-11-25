package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
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

public class DynamoFollowsDAO implements IFollowsDAO {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";
    private static final String AttrFollowerAlias = "followerAlias";
    private static final String AttrFolloweeAlias = "followeeAlias";

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
    public DataPage<FollowsTable> getFollowees(String followerAlias, int pageSize, String lastFolloweeAlias) {
        DynamoDbTable<FollowsTable> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTable.class));
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastFolloweeAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AttrFollowerAlias, AttributeValue.builder().s(followerAlias).build());
            startKey.put(AttrFolloweeAlias, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsTable> result = new DataPage<>();

        PageIterable<FollowsTable> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowsTable> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(followee -> result.getValues().add(followee));
                });

        return result;
    }

    @Override
    public DataPage<FollowsTable> getFollowers(String followeeAlias, int pageSize, String lastFollowerAlias) {
        DynamoDbIndex<FollowsTable> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTable.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AttrFolloweeAlias, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(AttrFollowerAlias, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsTable> result = new DataPage<>();

        SdkIterable<Page<FollowsTable>> sdkIterable = index.query(request);
        PageIterable<FollowsTable> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowsTable> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follower -> result.getValues().add(follower));
                });

        return result;
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowsTable> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTable.class));
        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();
        FollowsTable follow = table.getItem(key);
        return follow != null;
    }

    @Override
    public void follow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowsTable> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTable.class));
        FollowsTable newFollow = new FollowsTable();
        newFollow.setFollowerAlias(followerAlias);
        newFollow.setFolloweeAlias(followeeAlias);
        table.putItem(newFollow);
    }

    @Override
    public void unfollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowsTable> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTable.class));
        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();
        table.deleteItem(key);
    }
}
