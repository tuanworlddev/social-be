package org.tuandev.socialbe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FriendResponse {
    private int id;
    private UserResponse friend;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
