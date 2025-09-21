package org.example.gamearenax_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Brackets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tournamentId;
    private int round;
    private int matchNumber;

    @ManyToOne
    @JoinColumn(name = "player_a_id")
    private TournamentParticipant playerA;

    @ManyToOne
    @JoinColumn(name = "player_b_id")
    private TournamentParticipant playerB;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private TournamentParticipant winner;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.PENDING;
}

