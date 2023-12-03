package edu.byu.cs.tweeter.server.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FeedTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.DynamoFollowsDAO;
import edu.byu.cs.tweeter.server.lambda.util.UpdateFeed;
import edu.byu.cs.tweeter.server.service.FollowService;

public class TestCode extends DynamoFollowsDAO {
    public static void main(String[] args) {
        Status status = new Status("SQS hello", new User("Sherry", "Cousin", "@sc", "imageURL"), 1701623292000L, new ArrayList<>(), new ArrayList<>());
        DynamoFollowsDAO followsDAO = new DynamoFollowsDAO();
        DataPage<FollowsTable> result = followsDAO.getFollowers("@sc", 25, "@ms7");
        List<String> followersAliases = FollowService.getFollowerAliases(result);
        System.out.println(followersAliases);
        System.out.println(result.isHasMorePages());

        ObjectMapper mapper = new ObjectMapper();
        UpdateFeed updateFeed = new UpdateFeed(followersAliases, status);


        List<FeedTable> feedEntries = new ArrayList<>();
        for (String alias : updateFeed.getFollowersAliases()) {
            System.out.println(alias);FeedTable feedEntry = new FeedTable();
            feedEntry.setAuthorAlias(updateFeed.getStatus().getUser().getAlias());
            feedEntry.setPost(updateFeed.getStatus().getPost());
            feedEntry.setTimestamp(updateFeed.getStatus().getTimestamp());
            feedEntry.setUrls(updateFeed.getStatus().getUrls());
            feedEntry.setMentions(updateFeed.getStatus().getMentions());
            feedEntry.setBelongToAlias(alias);
            feedEntries.add(feedEntry);
        }
        for (int i = 0; i < feedEntries.size(); i++)
            System.out.println(feedEntries.get(i).toString());
    }
}
