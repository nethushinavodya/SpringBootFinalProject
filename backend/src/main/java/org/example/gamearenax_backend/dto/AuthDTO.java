package org.example.gamearenax_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthDTO {
    private UUID uuid;
    private String email;
    private String role;
    private String token;
    private String username;

    public AuthDTO(String email, String role, String token, String username) {
        this.email = email;
        this.role = role;
        this.token = token;
        this.username = username;
    }
}
