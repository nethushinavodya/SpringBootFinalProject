package org.example.gamearenax_backend.dto;

import lombok.*;
import org.example.gamearenax_backend.entity.User;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDTO {
    private UUID playerId;
    private String email;
    private String playerName;
    private String role;
    private String country;

    private String imageUrl;
    private boolean isOnline;
    private String about;

    private int totalMatches;
    private int wins;
    private String rank;

    private String status;
    private User user;
    private List<String> games;

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setOnline(Object o) {
        this.isOnline = (boolean) o;
    }
}