package org.tuandev.socialbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tuandev.socialbe.services.WaitingUserService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class WaitingUserServiceImpl implements WaitingUserService {
    private final Queue<Integer> waitingUserQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void addUserWaiting(int id) {
        System.out.println("Adding user to queue: " + id);
        waitingUserQueue.add(id);
    }

    @Override
    public void removeUserWaiting(int id) {
        System.out.println("Removing user from queue: " + id);
        waitingUserQueue.remove(id);
    }

    @Override
    public int getUserWaitingCount() {
        int size = waitingUserQueue.size();
        System.out.println("Current queue size: " + size);
        return size;
    }

    @Override
    public int pollUserWaiting() {
        Integer id = waitingUserQueue.poll();
        System.out.println("Polling user from queue: " + id);
        return id != null ? id : -1;
    }

}
