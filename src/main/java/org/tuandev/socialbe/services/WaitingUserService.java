package org.tuandev.socialbe.services;

public interface WaitingUserService {
    void addUserWaiting(int id);
    void removeUserWaiting(int id);
    int getUserWaitingCount();
    int pollUserWaiting();
}
