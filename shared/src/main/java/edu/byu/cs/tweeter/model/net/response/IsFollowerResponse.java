package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    private boolean follow;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public IsFollowerResponse(Boolean success, Boolean isFollow) {
        super(success);
        this.follow = isFollow;
    }

    public boolean isFollow() {
        return follow;
    }
}
