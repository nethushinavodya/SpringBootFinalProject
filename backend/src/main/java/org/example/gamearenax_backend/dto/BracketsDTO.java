package org.example.gamearenax_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.example.gamearenax_backend.entity.MatchStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BracketsDTO {
    private Long id;
    private Long tournamentId;
    private int round;
    private int matchNumber;
    private TournamentParticipantDTO playerA;
    private TournamentParticipantDTO playerB;
    private TournamentParticipantDTO winner;
    private MatchStatus status;
}