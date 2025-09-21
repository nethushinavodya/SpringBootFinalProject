package org.example.gamearenax_backend.dto;

import jakarta.persistence.GeneratedValue;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class JoinRequestDTO {
    private String playerId;
    private String clanId;
    private String message;
    private String status;
}
