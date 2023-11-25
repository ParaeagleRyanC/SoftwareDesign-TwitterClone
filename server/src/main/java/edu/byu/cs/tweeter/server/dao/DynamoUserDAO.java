package edu.byu.cs.tweeter.server.dao;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.UserTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoUserDAO extends DynamoDAO implements IUserDAO {

    @Override
    public User login(String username, String password) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(username)
                .build();
        UserTable user = table.getItem(key);
        if (user == null) return null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) return null;
        return new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImage());
    }

    @Override
    public User register(String alias, String password, String firstname, String lastname, String imageString) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        if (table.getItem(key) == null) {
            IImageUploadDAO imageDAO = new DynamoDbDAOFactory().getImageUploadDAO();
            String imageUrl = imageDAO.uploadImage(imageString, alias);
            UserTable newUser = new UserTable();
            newUser.setAlias(alias);
            newUser.setPassword(password);
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setImage(imageUrl);
            newUser.setFollowerCount(0);
            newUser.setFollowingCount(0);
            table.putItem(newUser);
            return new User(firstname, lastname, alias, imageUrl);
        }
        return null;
    }

    @Override
    public User getUser(String alias) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        UserTable user = table.getItem(key);
        if (user != null)
            return new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImage());
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public int getFollowerCount(String alias) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        UserTable user = table.getItem(key);
        return user.getFollowerCount();
    }

    @Override
    public int getFollowingCount(String alias) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        UserTable user = table.getItem(key);
        return user.getFollowingCount();
    }

    @Override
    public void updateFollowingCount(String alias, int count) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        UserTable user = table.getItem(key);
        user.setFollowingCount(user.getFollowingCount() + count);
        table.updateItem(user);
    }

    @Override
    public void updateFollowerCount(String alias, int count) {
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        UserTable user = table.getItem(key);
        user.setFollowerCount(user.getFollowerCount() + count);
        table.updateItem(user);
    }

    @Override
    public List<User> getUsers(List<String> aliases) {
        List<User> result = new ArrayList<>();
        DynamoDbTable<UserTable> table = enhancedClient.table(UsersTableName, TableSchema.fromBean(UserTable.class));
        for (String alias : aliases) {
            Key key = Key.builder()
                    .partitionValue(alias)
                    .build();
            UserTable user = table.getItem(key);
            if (user != null) {
                result.add(new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImage()));
            }
         }
        return result;
    }
}
