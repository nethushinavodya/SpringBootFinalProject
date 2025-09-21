package org.example.gamearenax_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TournamentParticipantDTO {
    private Long id;
    private Long tournamentId;
    private String clanId;
    private String clanName;
    private String playerId;
    private String playerEmail;
    private int remainingSlots;
}
