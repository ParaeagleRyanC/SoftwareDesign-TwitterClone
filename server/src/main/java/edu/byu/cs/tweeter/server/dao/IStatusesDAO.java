package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.StoriesTable;

public interface IStatusesDAO {
    DataPage<StoriesTable> getStories(String userAlias, Status lastStatus, int limit);
    DataPage<FeedTable> getFeed(String userAlias, Status lastStatus, int limit);
    void postStatus(Status status, List<String> followerAliases);
    void deleteStatus(User user, long timestamp);
}
