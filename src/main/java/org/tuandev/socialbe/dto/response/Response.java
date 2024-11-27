package org.tuandev.socialbe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Response {
    private int code;
    private String message;
    private Object data;
    private Object error;
    private LocalDateTime timestamp;
}
