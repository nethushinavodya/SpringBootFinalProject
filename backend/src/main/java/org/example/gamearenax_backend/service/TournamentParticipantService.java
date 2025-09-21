package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.TournamentParticipantDTO;
import org.springframework.stereotype.Service;

public interface TournamentParticipantService {
    TournamentParticipantDTO joinTournament(TournamentParticipantDTO tournamentParticipantDTO);

    Object getParticipantsByTournamentId(String tournamentId);

    Object getTournamentsByPlayerEmail(String playerEmail);
}
