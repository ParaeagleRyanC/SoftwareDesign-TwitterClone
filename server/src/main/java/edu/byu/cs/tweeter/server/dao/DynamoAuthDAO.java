package edu.byu.cs.tweeter.server.dao;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.AuthTokenTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoAuthDAO extends DynamoDAO implements IAuthTokenDAO {
    private static final long TokenTimeOutMillis = 600000; // 10 minutes

    @Override
    public AuthToken addToken() {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(AuthTokenTableName, TableSchema.fromBean(AuthTokenTable.class));
        AuthTokenTable newToken = new AuthTokenTable();
        newToken.setAuthToken(String.valueOf(UUID.randomUUID()));
        newToken.setTimestamp(System.currentTimeMillis());
        table.putItem(newToken);
        return new AuthToken(newToken.getAuthToken(), newToken.getTimestamp());
    }

    @Override
    public boolean validateToken(String token) {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(AuthTokenTableName, TableSchema.fromBean(AuthTokenTable.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        AuthTokenTable retrievedToken = table.getItem(key);
        if (retrievedToken == null) return false;
        if (retrievedToken.getTimestamp() + TokenTimeOutMillis < System.currentTimeMillis()) {
            deleteToken(token);
        }
        return true;
    }

    @Override
    public void deleteToken(String token) {
        DynamoDbTable<AuthTokenTable> table = enhancedClient.table(AuthTokenTableName, TableSchema.fromBean(AuthTokenTable.class));
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        table.deleteItem(key);
    }
}
