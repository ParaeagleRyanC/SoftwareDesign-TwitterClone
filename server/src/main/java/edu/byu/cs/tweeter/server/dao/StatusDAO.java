package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'statuses' data from the database.
 */
public class StatusDAO {


    public Pair<List<Status>, Boolean> getStatuses(Status lastStatus, int limit) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;

        List<Status> allStatuses = getFakeData().getFakeStatuses();
        List<Status> responseStatuses = new ArrayList<>(limit);
        boolean hasMorePages = false;

        if(limit > 0) {
            if (allStatuses != null) {
                int statusesIndex = getStatusesStartingIndex(lastStatus, allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < limit; statusesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusesIndex));
                }

                hasMorePages = statusesIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseStatuses, hasMorePages);
    }

    private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.getUser().getAlias().equals(allStatuses.get(i).getUser().getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }

        return statusIndex;
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
