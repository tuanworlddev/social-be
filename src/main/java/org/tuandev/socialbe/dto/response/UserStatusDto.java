package org.tuandev.socialbe.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserStatusDto {
    private int userId;
    private String status;
}
