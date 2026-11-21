package com.ada.incident.user;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password) {
}
