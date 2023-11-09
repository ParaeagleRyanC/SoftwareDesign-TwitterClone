package edu.byu.cs.tweeter.client.model.services;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetPagedItemHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends GeneralService {

    public static final String GET_FEED_URL_PATH = "/getfeed";
    public static final String GET_STORIES_URL_PATH = "/getstories";
    public static final String POST_STATUS_URL_PATH = "/poststatus";

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedPresenter.GetPageItemObserver observer) {

        GetFeedTask getFeedTask = new GetFeedTask(authToken,
                user, pageSize, lastStatus, new GetPagedItemHandler<Status>(observer));
        execute(getFeedTask);
    }

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedPresenter.GetPageItemObserver observer) {

        GetStoryTask getStoryTask = new GetStoryTask(authToken,
                user, pageSize, lastStatus, new GetPagedItemHandler<Status>(observer));
        execute(getStoryTask);
    }

    public void postStatus(AuthToken authToken, Status newStatus, MainPresenter.PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                newStatus, new PostStatusHandler(observer));
        execute(statusTask);
    }
}
