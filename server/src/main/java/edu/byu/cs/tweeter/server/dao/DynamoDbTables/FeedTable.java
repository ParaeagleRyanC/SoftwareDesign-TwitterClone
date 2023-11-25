package edu.byu.cs.tweeter.server.dao.DynamoDbTables;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedTable {
    private String belongToAlias;
    private String post;
    private String authorAlias;
    private Long timestamp;
    private List<String> urls;
    private List<String> mentions;

    @DynamoDbPartitionKey
    public String getBelongToAlias() {
        return belongToAlias;
    }

    public void setBelongToAlias(String belongToAlias) {
        this.belongToAlias = belongToAlias;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAuthorAlias() {
        return authorAlias;
    }

    public void setAuthorAlias(String alias) {
        this.authorAlias = alias;
    }

    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
