package org.example.gamearenax_backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClanMemberDTO {
    private UUID clanId;
    private UUID playerId;
    private String role;          // LEADER, CO_LEADER, PLAYER, STREAMER
}
