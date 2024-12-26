package org.tuandev.socialbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tuandev.socialbe.entities.Message;
import org.tuandev.socialbe.repositories.MessageRepository;
import org.tuandev.socialbe.services.MessageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getMessagesBetweenFriends(int userId1, int userId2) {
        return messageRepository.findMessagesBetweenUsers(userId1, userId2);
    }

    @Override
    public Message saveMessage(int senderId, int receiverId, String content) {
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .status(Message.MessageStatus.SENT)
                .sentAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    @Override
    public Message editMessage(String messageId, String newContent) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setContent(newContent);
        message.setStatus(Message.MessageStatus.EDITED);
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public void recallMessage(String messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setContent("This message has been recalled.");
        message.setStatus(Message.MessageStatus.RECALLED);
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }
}
