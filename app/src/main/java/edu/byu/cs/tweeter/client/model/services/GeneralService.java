package edu.byu.cs.tweeter.client.model.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeneralService {
    public <T> void execute(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute((Runnable) task);
    }
}
