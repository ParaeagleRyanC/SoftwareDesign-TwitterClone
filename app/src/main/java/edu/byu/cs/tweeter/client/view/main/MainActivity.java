package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.MainView;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements StatusDialogFragment.Observer, MainView {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";

    private Toast logOutToast;
    private Toast postingToast;
    private Toast toast;
    private User selectedUser;
    private TextView followeeCount;
    private TextView followerCount;
    private Button followButton;

    private MainPresenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if (selectedUser == null) {
            throw new RuntimeException("User not passed to activity");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), selectedUser);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
                statusDialogFragment.show(getSupportFragmentManager(), "post-status-dialog");
            }
        });

        presenter.getFollowCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser);

        TextView userName = findViewById(R.id.userName);
        userName.setText(selectedUser.retrieveFullName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(selectedUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        Picasso.get().load(selectedUser.getImageUrl()).into(userImageView);

        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, "..."));

        followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, "..."));

        followButton = findViewById(R.id.followButton);

        if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            followButton.setVisibility(View.GONE);
        } else {
            followButton.setVisibility(View.VISIBLE);
            presenter.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                    Cache.getInstance().getCurrUser(), selectedUser);
        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followButton.setEnabled(false);
                presenter.followUnfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                        followButton.getText().toString().equals(v.getContext().getString(R.string.following)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            presenter.logout(Cache.getInstance().getCurrUserAuthToken());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStatusPosted(String post) {
        presenter.postStatus(post, Cache.getInstance().getCurrUser(), Cache.getInstance().getCurrUserAuthToken());
    }

    public void updateFollowButton(boolean removed) {
        // If follow relationship was removed.
        if (removed) {
            followButton.setText(R.string.follow);
            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            followButton.setText(R.string.following);
            followButton.setBackgroundColor(getResources().getColor(R.color.white));
            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        }
    }

    @Override
    public void showInfoMessage(String message) {
        toast =  Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void showErrorMessage(String message) {
        toast =  Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void hideInfoMessage() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    @Override
    public void logoutUser() {
        //Revert to login screen.
        Intent intent = new Intent(this, LoginActivity.class);
        //Clear everything so that the main activity is recreated with the login page.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void updateFollowCount(int count, boolean isFollowerCount) {
        if (isFollowerCount) {
            followerCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
        }
        else {
            followeeCount.setText(getString(R.string.followerCount, String.valueOf(count)));
        }
    }

    @Override
    public void followUnfollowSuccess(boolean canFollow) {
        presenter.getFollowCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser);
        updateFollowButton(canFollow);
        followButton.setEnabled(true);
    }

    @Override
    public void isFollowerDisplay(boolean isFollower) {
        if (isFollower) {
            followButton.setText(R.string.following);
            followButton.setBackgroundColor(getResources().getColor(R.color.white));
            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        }
        else {
            followButton.setText(R.string.follow);
            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
