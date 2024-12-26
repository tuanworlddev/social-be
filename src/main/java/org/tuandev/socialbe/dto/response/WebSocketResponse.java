package org.tuandev.socialbe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WebSocketResponse {
    private Integer sender;
    private Integer receiver;
    private Object message;
    private String type;
}
