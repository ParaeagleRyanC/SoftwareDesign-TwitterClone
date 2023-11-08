package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        List<Status> statuses = null;
        boolean morePages = false;
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            StatusesRequest request = new StatusesRequest(targetUserAlias, limit, lastItem);
            StatusesResponse response = getServerFacade().getStatuses(request, StatusService.GET_STORIES_URL_PATH);

            if (response.isSuccess()) {
                statuses = response.getStatuses();
                morePages = response.getHasMorePages();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get statues", ex);
            sendExceptionMessage(ex);
        }
        return new Pair<>(statuses, morePages);
    }
}
