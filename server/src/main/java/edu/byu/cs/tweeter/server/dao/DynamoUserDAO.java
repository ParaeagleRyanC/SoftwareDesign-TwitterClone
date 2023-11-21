package edu.byu.cs.tweeter.server.dao;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDbTables.UserTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoUserDAO implements IUserDAO {

    private static final String TableName = "users";
    private static final String AttrAlias = "alias";
    private static final String AttrPassword = "password";
    private static final String AttrFirstName = "firstName";
    private static final String AttrLastName = "lastName";
    private static final String AttrImage = "image";

    // DynamoDB client
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    @Override
    public User login(String username, String password) {
        DynamoDbTable<UserTable> table = enhancedClient.table(TableName, TableSchema.fromBean(UserTable.class));
        Key key = Key.builder()
                .partitionValue(username)
                .build();
        UserTable user = table.getItem(key);
        if (user == null) return null;
        if (!Objects.equals(user.getPassword(), password)) return null;
        return new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImage());
    }

    @Override
    public User register(String alias, String password, String firstname, String lastname, String imageString) {
        DynamoDbTable<UserTable> table = enhancedClient.table(TableName, TableSchema.fromBean(UserTable.class));
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
            table.putItem(newUser);
            return new User(firstname, lastname, alias, imageUrl);
        }
        return null;
    }

    @Override
    public User getUser(String alias) {
        DynamoDbTable<UserTable> table = enhancedClient.table(TableName, TableSchema.fromBean(UserTable.class));
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
}
