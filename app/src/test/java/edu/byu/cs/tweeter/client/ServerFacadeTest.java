package edu.byu.cs.tweeter.client;

import android.util.Log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowsCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticatedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowsResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowsCountResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {
    private final String LOG_TAG = "ServerFacadeTest";

    private static ServerFacade serverFacade;
    private static FakeData fakeData;

    @BeforeAll
    public static void setup() {
        serverFacade = new ServerFacade();
        fakeData = FakeData.getInstance();
    }

    @Test
    public void registerTest() {
        RegisterRequest registerRequest = new RegisterRequest(
                "aUsername", "aPassword",
                "aFirstName", "aLastName", "anImage");
        try {
            AuthenticatedResponse registerResponse = serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);
            Assertions.assertEquals(registerResponse.getUser(), fakeData.getFirstUser());
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
    }

    @Test
    public void getFollowersTest() {
        FollowsRequest followsRequest = new FollowsRequest(fakeData.getAuthToken(), "followerAlias",
                                            10, "followeeAlias");

        try {
            FollowsResponse followsResponse = serverFacade.getFollows(followsRequest, FollowService.GET_FOLLOWER_URL_PATH);
            Assertions.assertEquals(followsResponse.getFollows(), fakeData.getFakeUsers().subList(0,10));
            Assertions.assertTrue(followsResponse.getHasMorePages());
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
    }

    @Test
    public void getFollowerCountTest() {
        GetFollowsCountRequest getFollowsCountRequest = new GetFollowsCountRequest("targetAlias");

        try {
            GetFollowsCountResponse getFollowsCountResponse = serverFacade.getFollowerCount(getFollowsCountRequest, FollowService.GET_FOLLOWER_COUNT_URL_PATH);
            Assertions.assertEquals(getFollowsCountResponse.getCount(), 30);
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
    }
}
