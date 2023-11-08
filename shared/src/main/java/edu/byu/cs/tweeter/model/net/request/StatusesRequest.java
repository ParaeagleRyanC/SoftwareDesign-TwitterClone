package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class StatusesRequest {

    private Status lastStatus;
    private String targetAlias;
    private int limit;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StatusesRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetAlias the alias of the user whose statuses are to be returned.
     * @param limit the maximum number of followees to return.
     */
    public StatusesRequest(String targetAlias, int limit, Status status) {
        this.targetAlias = targetAlias;
        this.limit = limit;
        this.lastStatus = status;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
