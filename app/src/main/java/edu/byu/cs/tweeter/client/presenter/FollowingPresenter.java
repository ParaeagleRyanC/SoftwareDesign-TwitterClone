package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PageView view, User user) {
        super(view, user);
    }

    @Override
    public void getItems() {
        var followService = new FollowService();
        followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new GetPageItemObserver());
    }
}