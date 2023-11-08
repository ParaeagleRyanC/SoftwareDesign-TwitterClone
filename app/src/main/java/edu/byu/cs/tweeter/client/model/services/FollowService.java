package edu.byu.cs.tweeter.client.model.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetPagedItemHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends GeneralService {

    public static final String GET_FOLLOWING_URL_PATH = "/getfollowing";
    public static final String GET_FOLLOWER_URL_PATH = "/getfollower";
    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";
    public static final String GET_FOLLOWING_COUNT_URL_PATH = "/getfollowingcount";
    public static final String GET_FOLLOWER_COUNT_URL_PATH = "/getfollowercount";

    public void getFollowing(AuthToken authToken, User user, int pageSize,
                             User lastFollowee, PagedPresenter.GetPageItemObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken, user, pageSize,
                lastFollowee, new GetPagedItemHandler<User>(observer));
        execute(getFollowingTask);
    }

    public void getFollower(AuthToken authToken, User user, int pageSize,
                            User lastFollower, PagedPresenter.GetPageItemObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, pageSize,
                lastFollower, new GetPagedItemHandler<User>(observer));
        execute(getFollowersTask);
    }

    public void followUnfollow(AuthToken authToken, User selectedUser, MainPresenter.FollowUnfollowObserver observer, boolean following) {
        if (!following) {
            FollowTask followTask = new FollowTask(authToken,
                    selectedUser, new FollowHandler(observer));
            execute(followTask);
        }
        else {
            UnfollowTask unfollowTask = new UnfollowTask(authToken,
                    selectedUser, new UnfollowHandler(observer));
            execute(unfollowTask);
        }
    }

    public void isFollower(AuthToken authToken, User follower, User followee, MainPresenter.IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, follower, followee,
                                                    new IsFollowerHandler(observer));
        execute(isFollowerTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User selectedUser, MainPresenter.GetFollowCountObserver observer) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }
}
