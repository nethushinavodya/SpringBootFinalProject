package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.TournamentDTO;
import org.example.gamearenax_backend.entity.Game;

public interface TournamentService {
    int addTournament(TournamentDTO tournamentDTO,Game game);

    int updateTournament(TournamentDTO tournamentDTO);

    Object getAllTournaments();

    Object getTournamentByOngoingStatus();

    Object getUpcomingTournaments();

    Object getSoloTournaments();

    Object getClanTournaments();

    Object updateRegistrationStatus(String tournamentId, String registrationStatus);

    Object getTournamentByStreamer(String email);

    Object getTournamentById(String id);
}
