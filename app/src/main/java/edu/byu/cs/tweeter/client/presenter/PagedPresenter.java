package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.view.PageView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BasePresenter<PageView> {

    public class GetPageItemObserver implements ServiceObserver {
        public void getItemsSucceeded(List<T> items, boolean hasMorePages) {
            isLoading = false;
            lastItem = (items.size() > 0 ? items.get(items.size() - 1) : null);
            setHasMorePages(hasMorePages);
            view.endLoading();
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.endLoading();
            view.showErrorMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.showErrorMessage("Failed to get page items because of exception: " + exception.getMessage());
        }
    }

    public class GetUserObserver implements ServiceObserver {
        public  void getUserSucceeded(User user) {
            view.openMainView(user);
        }
        @Override
        public void handleFailure(String message) {
            view.showErrorMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.showErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
    public PagedPresenter(PageView view, User user) {
        super(view);
        this.user = user;
    }

    protected static final int PAGE_SIZE = 10;
    protected User user;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    public void getUser(AuthToken authToken, String alias) {
        var userService = new UserService();
        userService.getUser(authToken, alias, new GetUserObserver());
        view.showInfoMessage("Getting user's profile...");
    }

    /**
     * Causes the Adapter to display a loading footer and make a request to get more following
     * data.
     */
    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.startLoading();
            getItems();
        }
    }

    public abstract void getItems();

    public boolean getIsLoading() {
        return isLoading;
    }
    public boolean getHasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) { this.hasMorePages = hasMorePages; }
}
