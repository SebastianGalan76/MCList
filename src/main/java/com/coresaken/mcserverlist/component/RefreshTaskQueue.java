package com.coresaken.mcserverlist.component;

import com.coresaken.mcserverlist.database.model.server.Server;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshTaskQueue {

    private final BlockingQueue<Server> queue = new LinkedBlockingQueue<>();

    public void addTask(Server server) {
        queue.offer(server);
    }

    public Server pollTask() throws InterruptedException {
        return queue.poll(1, TimeUnit.SECONDS);
    }
}