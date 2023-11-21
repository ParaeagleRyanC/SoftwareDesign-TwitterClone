package edu.byu.cs.tweeter.server.dao;

public interface IDAOFactory {
    IAuthTokenDAO getAuthDAO();
    IUserDAO getUserDAO();
    IFollowsDAO getFollowsDAO();
    IStatusesDAO getStatusDAO();
    IImageUploadDAO getImageUploadDAO();
}
