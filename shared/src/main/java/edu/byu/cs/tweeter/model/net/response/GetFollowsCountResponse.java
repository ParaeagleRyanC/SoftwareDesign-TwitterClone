package edu.byu.cs.tweeter.model.net.response;

public class GetFollowsCountResponse extends Response {

    private int count;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetFollowsCountResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count follower/following count.
     */
    public GetFollowsCountResponse(Boolean success, int count) {
        super(success, null);
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
