package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowsCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowsCountResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowerCountTask";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        int count = 0;
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        try {

            GetFollowsCountRequest request = new GetFollowsCountRequest(targetUserAlias);
            GetFollowsCountResponse response = getServerFacade().getFollowerCount(request, FollowService.GET_FOLLOWER_COUNT_URL_PATH);

            if (response.isSuccess()) {
                count = response.getCount();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get follower count", ex);
            sendExceptionMessage(ex);
        }
        return count;
    }
}
