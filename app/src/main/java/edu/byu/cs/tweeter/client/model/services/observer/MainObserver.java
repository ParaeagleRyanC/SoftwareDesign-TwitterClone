package edu.byu.cs.tweeter.client.model.services.observer;


import edu.byu.cs.tweeter.client.view.MainView;

public abstract class MainObserver implements ServiceObserver {

    protected MainView view;
    public MainObserver(MainView view) { this.view = view; }
    @Override
    public void handleFailure(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void handleException(Exception exception) {
        view.showErrorMessage(getExceptionMessage() + exception.getMessage());
    }

    public abstract String getExceptionMessage();
}
