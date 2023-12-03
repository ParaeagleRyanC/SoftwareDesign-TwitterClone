package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.observer.MainObserver;
import edu.byu.cs.tweeter.client.view.MainView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainView> {

    public static class LogoutObserver extends MainObserver {
        public LogoutObserver(MainView view) {
            super(view);
        }

        public void logoutSucceeded() {
            view.logoutUser();
        }
        @Override
        public String getExceptionMessage() {
            return "Failed to logout because of exception: ";
        }
    }

    public static class PostStatusObserver extends MainObserver {
        public PostStatusObserver(MainView view) {
            super(view);
        }

        public void postStatusSucceeded(String message) {
            view.hideInfoMessage();
            view.showInfoMessage(message);
        }

        @Override
        public String getExceptionMessage() {
            return "Failed to post status because of exception: ";
        }
    }

    public static class IsFollowerObserver extends MainObserver {
        public IsFollowerObserver(MainView view) {
            super(view);
        }

        public void isFollowSucceeded(boolean isFollower) {
            view.isFollowerDisplay(isFollower);
        }

        @Override
        public String getExceptionMessage() {
            return "Failed to determine following relationship because of exception: ";
        }
    }

    public static class FollowUnfollowObserver extends MainObserver {
        public FollowUnfollowObserver(MainView view) {
            super(view);
        }

        public void followUnfollowSucceeded(boolean canFollow) {
            view.followUnfollowSuccess(canFollow);
        }

        @Override
        public String getExceptionMessage() {
            return "Failed to (un)follow because of exception: ";
        }
    }

    public static class GetFollowCountObserver extends MainObserver {
        public GetFollowCountObserver(MainView view) {
            super(view);
        }

        public void getFollowCountSucceed(int count, boolean isFollowerCount) {
            view.updateFollowCount(count, isFollowerCount);
        }
        @Override
        public String getExceptionMessage() {
            return "Failed to get followers count because of exception: ";
        }
    }

    public MainPresenter(MainView view) {
        super(view);
    }

    public void logout(AuthToken authToken) {
        view.showInfoMessage("Logging Out...");
        var userService = new UserService();
        userService.logout(authToken, new LogoutObserver(view));
    }

    public void getFollowCount(AuthToken authToken, User selectedUser) {
        var followService = new FollowService();
        followService.updateSelectedUserFollowingAndFollowers(authToken, selectedUser, new GetFollowCountObserver(view));
    }

    public void followUnfollow(AuthToken authToken, User selectedUser, boolean following) {
        if (following) {
            view.showInfoMessage("Removing " + selectedUser.retrieveFullName() + "...");
        }
        else {
            view.showInfoMessage("Adding " + selectedUser.retrieveFullName() + "...");
        }
        var followService = new FollowService();
        followService.followUnfollow(authToken, selectedUser, new FollowUnfollowObserver(view), following);
    }

    public void isFollower(AuthToken authToken, User follower, User followee) {
        var followService = new FollowService();
        followService.isFollower(authToken, follower, followee, new IsFollowerObserver(view));
    }

    private StatusService statusService;
    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void postStatus(String post, User user, AuthToken authToken) {
        view.showInfoMessage("Posting Status...");
        Status newStatus = new Status(post, user, System.currentTimeMillis(),
                                      parseURLs(post), parseMentions(post));
        getStatusService().postStatus(authToken, newStatus, new PostStatusObserver(view));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

}
