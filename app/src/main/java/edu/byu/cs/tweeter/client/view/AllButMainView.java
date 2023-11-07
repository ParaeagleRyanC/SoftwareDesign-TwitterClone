package edu.byu.cs.tweeter.client.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface AllButMainView extends BaseView {
    void openMainView(User user);
}
