package edu.byu.cs.tweeter.server.dao.DynamoDbTables;

import edu.byu.cs.tweeter.server.dao.DynamoFollowsDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowsTable {
    private String followerAlias;
    private String followeeAlias;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoFollowsDAO.IndexName)
    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoFollowsDAO.IndexName)
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
