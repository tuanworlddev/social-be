package org.tuandev.socialbe.services;

import org.tuandev.socialbe.entities.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesBetweenFriends(int userId1, int userId2);
    Message saveMessage(int senderId, int receiverId, String content);
    Message editMessage(String messageId, String newContent);
    void recallMessage(String messageId);
}
