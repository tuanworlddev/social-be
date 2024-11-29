package org.tuandev.socialbe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuandev.socialbe.dto.request.AddFriendRequest;
import org.tuandev.socialbe.dto.request.UpdateFriendRequest;
import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.services.FriendService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping()
    public ResponseEntity<Response> getAllFriends() {
        List<FriendResponse> friendResponses = friendService.getAllFriends();
        return ResponseEntity.ok(Response.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(friendResponses)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/status")
    public ResponseEntity<Response> getFriendsByStatus(@RequestParam("status") String status) {
        try {
            Friend.FriendStatus friendStatus = Friend.FriendStatus.valueOf(status);
            List<FriendResponse> users = friendService.getFriendsByStatus(friendStatus);
            return ResponseEntity.ok(Response.builder()
                    .code(200)
                    .data(users)
                    .message("Success")
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Response.builder()
                    .code(400)
                    .message("Invalid status value")
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping
    public ResponseEntity<Response> addFriend(@RequestBody AddFriendRequest addFriendRequest) {
        friendService.addFriend(addFriendRequest.friendId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.builder()
                .code(201)
                .message("Friend added successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/{friendId}")
    public ResponseEntity<Response> updateFriend(@PathVariable("friendId") Integer friendId, @RequestBody UpdateFriendRequest updateFriendRequest) {
        try {
            friendService.updateStatus(friendId, Friend.FriendStatus.valueOf(updateFriendRequest.status()));
            return ResponseEntity.status(HttpStatus.OK).body(Response.builder()
                    .code(200)
                    .message("Friend updated successfully")
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Response.builder()
                    .code(400)
                    .message("Invalid status value")
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }
}
