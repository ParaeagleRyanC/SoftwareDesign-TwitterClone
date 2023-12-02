package edu.byu.cs.tweeter.server.dao;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.DynamoDbTables.FollowsTable;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.UserTable;

public class DatabaseFiller {
    public static void main(String[] args) {
        // filler();
    }

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@ac";


    private static void filler() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        DynamoDAO dynamoDAO = new DynamoDAO();

        List<FollowsTable> follows = new ArrayList<>();
        List<UserTable> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String firstName = "Yasmine";
            String lastName = i + " Lovie";
            String alias = "@yl" + i;
            String password = encoder.encode("yl");
            String imageUrl = "https://cs340-twitter-clone-images.s3.us-west-2.amazonaws.com/download.jpg";

            UserTable user = new UserTable();
            user.setAlias(alias);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setImage(imageUrl);
            user.setFollowingCount(1);
            user.setFollowerCount(0);
            users.add(user);

            FollowsTable follow = new FollowsTable();
            follow.setFollowerAlias(alias);
            follow.setFolloweeAlias(FOLLOW_TARGET);
            follows.add(follow);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            dynamoDAO.addItemsBatch(users, DynamoDAO.UsersTableName, UserTable.class);
        }
        if (follows.size() > 0) {
            dynamoDAO.addItemsBatch(follows, DynamoDAO.FollowsTableName, FollowsTable.class);
        }
    }
}
