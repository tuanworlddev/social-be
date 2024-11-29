package org.tuandev.socialbe.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public String sendMessage(String message, Principal principal) {
        return principal.getName() + ": " + message;
    }
}
