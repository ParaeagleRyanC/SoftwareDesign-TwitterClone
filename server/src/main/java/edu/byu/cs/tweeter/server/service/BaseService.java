package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.IAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.IFollowsDAO;
import edu.byu.cs.tweeter.server.dao.IStatusesDAO;
import edu.byu.cs.tweeter.server.dao.IUserDAO;

public class BaseService {
    protected static IAuthTokenDAO authTokenDAO;
    protected static IUserDAO userDAO;
    protected static IFollowsDAO followsDAO;
    protected static IStatusesDAO statusesDAO;
    protected BaseService(IDAOFactory factory) {
        authTokenDAO = factory.getAuthDAO();
        userDAO = factory.getUserDAO();
        followsDAO = factory.getFollowsDAO();
        statusesDAO = factory.getStatusDAO();
    }
}
