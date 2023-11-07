package edu.byu.cs.tweeter.client.view;

public interface MainView extends BaseView {
    void logoutUser();
    void updateFollowCount(int count, boolean isFollowerCount);
    void followUnfollowSuccess(boolean canFollow);
    void isFollowerDisplay(boolean isFollower);
}
