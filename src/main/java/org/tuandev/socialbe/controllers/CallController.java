package org.tuandev.socialbe.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.tuandev.socialbe.dto.response.UserStatusDto;
import org.tuandev.socialbe.dto.response.WebSocketResponse;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.services.UserService;
import org.tuandev.socialbe.services.WaitingUserService;

@Controller
@RequiredArgsConstructor
public class CallController {
    private final UserService userService;
    private final Gson gson;
    private final WaitingUserService waitingUserService;
    private final SimpMessagingTemplate messagingTemplate;
    private long countUsers = 0;

    @MessageMapping("/call/increment")
    @SendTo("/topic/call/count")
    public String increment() {
        ++countUsers;
        System.out.println("Count users: " + countUsers);
        return String.valueOf(countUsers);
    }
    @MessageMapping("/call/decrement")
    @SendTo("/topic/call/count")
    public String decrement() {
        --countUsers;
        System.out.println("Count users: " + countUsers);
        return String.valueOf(countUsers);
    }
    @MessageMapping("/call/count")
    @SendTo("/topic/call/count")
    public String countUser() {
        return String.valueOf(countUsers);
    }

    @MessageMapping("/user/status")
    @SendTo("/topic/user/status")
    public String userStatus(@Payload UserStatusDto userStatusDto) {
        System.out.println(userStatusDto.toString());
        userService.updateStatus(userStatusDto.getUserId(), User.UserStatus.valueOf(userStatusDto.getStatus()));
        return gson.toJson(userStatusDto);
    }

    @MessageMapping("/call/match")
    public void match(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        int receiverId = waitingUserService.pollUserWaiting();
        if (receiverId != -1 && receiverId != response.getSender()) {
            response.setReceiver(receiverId);
            messagingTemplate.convertAndSend("/queue/" + response.getSender() + "/call/match", response);
        } else {
            waitingUserService.addUserWaiting(response.getSender());
        }
    }

    @MessageMapping("/call/offer")
    public void offer(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        messagingTemplate.convertAndSend("/queue/" + response.getReceiver() + "/call/offer", response);
    }

    @MessageMapping("/call/answer")
    public void answer(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        messagingTemplate.convertAndSend("/queue/" + response.getReceiver() + "/call/answer", response);
    }

    @MessageMapping("/call/candidate")
    public void candidate(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        messagingTemplate.convertAndSend("/queue/" + response.getReceiver() + "/call/candidate", response);
    }

    @MessageMapping("/call/next")
    public void next(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        messagingTemplate.convertAndSend("/queue/" + response.getReceiver() + "/call/next", response);
    }

    @MessageMapping("/call/stop")
    public void stop(@Payload WebSocketResponse response) {
        System.out.println(response.toString());
        messagingTemplate.convertAndSend("/queue/" + response.getReceiver() + "/call/stop", response);
    }
}
