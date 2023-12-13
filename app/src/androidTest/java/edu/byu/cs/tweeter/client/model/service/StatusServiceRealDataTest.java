package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.observer.PageItemObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;

public class StatusServiceRealDataTest {

    private Status post;
    private ServerFacade serverFacade;
    private MainView view;
    private MainPresenter presenter;
    private StatusService statusServiceSpy;
    private StatusServiceRealDataTest.StatusServiceObserver statusServiceObserver;
    private CountDownLatch countDownLatch;
    private final String postString = "sixth post.";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        serverFacade = new ServerFacade();
        view = Mockito.mock(MainView.class);
        presenter = new MainPresenter(view);
        statusServiceSpy = Mockito.spy(new StatusService());
        statusServiceObserver = new StatusServiceObserver();
    }

    @Test
    public void testPostStatus() throws InterruptedException, IOException, TweeterRemoteException {
        login();

        // post a status
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                countDownLatch.countDown();
                return null;
            }
        }).when(view).showInfoMessage("Successfully Posted!");

        resetCountDownLatch();
        presenter.postStatus(postString, Cache.getInstance().getCurrUser(), Cache.getInstance().getCurrUserAuthToken());
        awaitCountDownLatch();

        Mockito.verify(view).showInfoMessage("Posting Status...");
        Mockito.verify(view).showInfoMessage("Successfully Posted!");


        // get stories and verify the latest post is there and correct
        resetCountDownLatch();
        statusServiceSpy.getStory(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), 10, null, statusServiceObserver);
        awaitCountDownLatch();
        List<Status> statuses = statusServiceObserver.getStatuses();
        Status mostRecentStatus = statuses.get(statuses.size() - 1);
        Assertions.assertEquals(post.getPost(), mostRecentStatus.getPost());
        Assertions.assertEquals(post.getMentions(), mostRecentStatus.getMentions());
        Assertions.assertEquals(post.getUser(), mostRecentStatus.getUser());
        Assertions.assertEquals(post.getUrls(), mostRecentStatus.getUrls());
    }

    private void login() throws IOException, TweeterRemoteException, InterruptedException {
        System.out.println("Logging in...");
        AuthenticatedResponse loginResponse = serverFacade.login(new LoginRequest("@bc", "bc"), UserService.LOGIN_URL_PATH);
        Cache.getInstance().setCurrUserAuthToken(loginResponse.getAuthToken());
        Cache.getInstance().setCurrUser(loginResponse.getUser());
        post = new Status(postString, Cache.getInstance().getCurrUser(), Cache.getInstance().getCurrUserAuthToken().getTimestamp(), new ArrayList<>(), new ArrayList<>());
    }


    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    public class StatusServiceObserver implements PageItemObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> statuses;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void getItemsSucceeded(List<Status> statuses, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.statuses = statuses;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }
}
