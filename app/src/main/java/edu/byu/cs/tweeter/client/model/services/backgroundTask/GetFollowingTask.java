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
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    private static final String LOG_TAG = "GetFollowingTask";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        List<User> userList = null;
        boolean morePages = false;
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            String lastFolloweeAlias = lastItem == null ? null : lastItem.getAlias();

            FollowsRequest request = new FollowsRequest(authToken, targetUserAlias, limit, lastFolloweeAlias);
            FollowsResponse response = getServerFacade().getFollows(request, FollowService.GET_FOLLOWING_URL_PATH);

            if (response.isSuccess()) {
                userList = response.getFollows();
                morePages = response.getHasMorePages();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
        return new Pair<>(userList, morePages);
    }
}
