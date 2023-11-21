package edu.byu.cs.tweeter.server.dao;

public class DynamoDbDAOFactory implements IDAOFactory {
    @Override
    public IAuthTokenDAO getAuthDAO() {
        return new DynamoAuthDAO();
    }

    @Override
    public IUserDAO getUserDAO() {
        return new DynamoUserDAO();
    }

    @Override
    public IFollowsDAO getFollowsDAO() {
        return new DynamoFollowsDAO();
    }

    @Override
    public IStatusesDAO getStatusDAO() {
        return new DynamoStatusesDAO();
    }

    @Override
    public IImageUploadDAO getImageUploadDAO() {
        return new S3ImageDAO();
    }
}
