package edu.byu.cs.tweeter.server.dao;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.AuthTokenTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoAuthDAO implements IAuthTokenDAO {
    private static final String TableName = "authTokens";
    private static final long TimeOutMillis = 600000; // 10 minutes

    // DynamoDB client
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    @Override
    public AuthToken addToken() {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenTable.class));
        AuthTokenTable newToken = new AuthTokenTable();
        newToken.setAuthToken(String.valueOf(UUID.randomUUID()));
        newToken.setTimestamp(System.currentTimeMillis());
        table.putItem(newToken);
        return new AuthToken(newToken.getAuthToken(), newToken.getTimestamp());
    }

    @Override
    public boolean validateToken(String token) {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenTable.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        AuthTokenTable retrievedToken = table.getItem(key);
        if (retrievedToken == null) return false;
        if (retrievedToken.getTimestamp() + TimeOutMillis < System.currentTimeMillis()) {
            deleteToken(token);
        }
        return true;
    }

    @Override
    public void deleteToken(String token) {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenTable.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        table.deleteItem(key);
    }
}
