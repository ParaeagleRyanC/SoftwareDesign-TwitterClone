package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;

/**
 * A paged response for a {@link FollowsRequest}.
 */
public class FollowsResponse extends PagedResponse {

    private List<User> follows;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowsResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param follows the followees to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public FollowsResponse(List<User> follows, boolean hasMorePages) {
        super(true, hasMorePages);
        this.follows = follows;
    }

    /**
     * Returns the followees for the corresponding request.
     *
     * @return the followees.
     */
    public List<User> getFollows() {
        return follows;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowsResponse that = (FollowsResponse) param;

        return (Objects.equals(follows, that.follows) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(follows);
    }
}
