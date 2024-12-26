package org.tuandev.socialbe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuandev.socialbe.dto.request.AddFriendRequest;
import org.tuandev.socialbe.dto.request.UpdateFriendRequest;
import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.services.FriendService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriends() {
        return ResponseEntity.ok(friendService.getFriends());
    }

    @GetMapping("/not-friends")
    public ResponseEntity<List<UserResponse>> getNotFriends() {
        return ResponseEntity.ok(friendService.getNotFriends());
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendResponse>> getPendingRequests() {
        return ResponseEntity.ok(friendService.getPendingRequests());
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody Map<String, Integer> request) {
        int friendId = request.get("friendId");
        friendService.sendFriendRequest(friendId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/requests/{requestId}")
    public ResponseEntity<Void> respondToRequest(
            @PathVariable int requestId,
            @RequestBody Map<String, String> response) {
        String status = response.get("response");
        friendService.respondToRequest(requestId, status);
        return ResponseEntity.ok().build();
    }
}
