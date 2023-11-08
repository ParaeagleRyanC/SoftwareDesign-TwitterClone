package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.view.PageView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(PageView view, User user) {
        super(view, user);
    }

    @Override
    public void getItems() {
        var statusService = new StatusService();
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                lastItem, new GetPageItemObserver());
    }
}