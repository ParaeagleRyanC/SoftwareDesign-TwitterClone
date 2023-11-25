package edu.byu.cs.tweeter.server.dao;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAO {
    protected static final String AuthTokenTableName = "authTokens";
    protected static final String UsersTableName = "users";
    protected static final String FollowsTableName = "follows";
    protected static final String StoriesTableName = "stories";
    protected static final String FeedTableName = "feed";

    // DynamoDB client
    protected static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    protected static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
}
