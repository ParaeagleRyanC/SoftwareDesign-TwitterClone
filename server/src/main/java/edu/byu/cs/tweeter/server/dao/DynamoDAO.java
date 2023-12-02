package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

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

    public <T> void addItemsBatch(List<T> items, String tableName, Class<T> tClass) {
        List<T> batchToWrite = new ArrayList<>();
        for (T item : items) {
            batchToWrite.add(item);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfItems(batchToWrite, tableName, tClass);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfItems(batchToWrite, tableName, tClass);
        }
    }

    private <T> void writeChunkOfItems(List<T> items, String tableName, Class<T> tClass) {
        if(items.size() > 25)
            throw new RuntimeException("Too many to write");

        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tClass));
        WriteBatch.Builder<T> writeBuilder = WriteBatch.builder(tClass).mappedTableResource(table);
        for (T item : items) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfItems(result.unprocessedPutItemsForTable(table), tableName, tClass);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
