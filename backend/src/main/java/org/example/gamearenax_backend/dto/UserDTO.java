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
public class UserDTO {
    private UUID uuid;
    private String email;
    private String password;
    private String username;
    private String role;
    private String country;
    private String profilePicture;
    private String status;

    public UserDTO(String email, String password) {
        this.email = email; 
        this.password = password;
    }
}
