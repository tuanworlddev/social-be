package org.tuandev.socialbe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Email must not be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Full name must not be null")
    private String fullName;

    private String avatar;
}
