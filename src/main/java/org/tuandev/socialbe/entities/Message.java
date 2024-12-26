package org.tuandev.socialbe.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private int senderId;
    private int receiverId;

    private String content;

    private MessageStatus status;

    private LocalDateTime sentAt;
    private LocalDateTime updatedAt;

    public enum MessageStatus {
        SENT, EDITED, RECALLED
    }
}
