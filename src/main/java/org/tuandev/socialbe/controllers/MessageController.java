package org.tuandev.socialbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.tuandev.socialbe.entities.Message;
import org.tuandev.socialbe.services.MessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable int userId1, @PathVariable int userId2) {
        return ResponseEntity.ok(messageService.getMessagesBetweenFriends(userId1, userId2));
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestParam int senderId, @RequestParam int receiverId, @RequestParam String content) {
        Message message = messageService.saveMessage(senderId, receiverId, content);
        messagingTemplate.convertAndSend("/queue/" + receiverId + "/messages", message);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable String messageId, @RequestParam String newContent) {
        return ResponseEntity.ok(messageService.editMessage(messageId, newContent));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> recallMessage(@PathVariable String messageId) {
        messageService.recallMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
