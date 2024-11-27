package org.tuandev.socialbe.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
