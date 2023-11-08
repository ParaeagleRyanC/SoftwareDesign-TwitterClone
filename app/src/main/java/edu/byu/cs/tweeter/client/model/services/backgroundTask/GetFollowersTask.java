package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    private static final String LOG_TAG = "GetFollowerTask";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        List<User> userList = null;
        boolean morePages = false;
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            String lastFollowerAlias = lastItem == null ? null : lastItem.getAlias();

            FollowsRequest request = new FollowsRequest(authToken, targetUserAlias, limit, lastFollowerAlias);
            FollowsResponse response = getServerFacade().getFollows(request, FollowService.GET_FOLLOWER_URL_PATH);

            if (response.isSuccess()) {
                userList = response.getFollows();
                morePages = response.getHasMorePages();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
        }
        return new Pair<>(userList, morePages);
    }
}
