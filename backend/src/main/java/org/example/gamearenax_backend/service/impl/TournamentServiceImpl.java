package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.TournamentDTO;
import org.example.gamearenax_backend.entity.*;
import org.example.gamearenax_backend.repository.GameRepo;
import org.example.gamearenax_backend.repository.StreamerRepo;
import org.example.gamearenax_backend.repository.TournamentRepo;
import org.example.gamearenax_backend.repository.UserRepo;
import org.example.gamearenax_backend.service.GameService;
import org.example.gamearenax_backend.service.TournamentService;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepo tournamentRepo;
    private final ModelMapper modelMapper;
    private final GameRepo gameRepo;
    private final GameService gameService;
    private final StreamerRepo streamerRepo;


    @Override
    public int addTournament(TournamentDTO tournamentDTO,Game game) {
        try {
            Streamer streamer = streamerRepo.getStreamersByEmail(tournamentDTO.getStreamerEmail());
            Tournament tournament = modelMapper.map(tournamentDTO, Tournament.class);
            tournament.setGame(game);
            tournament.setStreamer(streamer);
            if (tournament.getStatus() == null) {
                tournament.setStatus(TournamentStatus.UPCOMING);
            }
            tournamentRepo.save(tournament);
            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int updateTournament(TournamentDTO dto) {
        Tournament tournament = tournamentRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        tournament.setName(dto.getName());
        tournament.setDescription(dto.getDescription());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setPrizePool(dto.getPrizePool());
        tournament.setType(TournamentType.valueOf(dto.getType().toUpperCase()));
        tournament.setMaxParticipants(dto.getMaxParticipants());
        Game game = gameService.getGameByName(dto.getGameName());
        if (!game.isActive()){
            throw new RuntimeException("Game is not active");
        }
        tournament.setGame(game);

        tournamentRepo.save(tournament);
        return VarList.Created;
    }

    @Override
    public Object getAllTournaments() {
        try {
            return tournamentRepo.findAll();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getTournamentByOngoingStatus() {
        try {
            return tournamentRepo.findByStatus(TournamentStatus.ONGOING);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getUpcomingTournaments() {
        try {
            return tournamentRepo.findByStatus(TournamentStatus.UPCOMING);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getSoloTournaments() {
        try {
            return tournamentRepo.findByType(TournamentType.SOLO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getClanTournaments() {
        try {
            return tournamentRepo.findByType(TournamentType.CLAN);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateRegistrationStatus(String tournamentId, String registrationStatus) {
        try {
            Tournament tournament = tournamentRepo.findById(Long.parseLong(tournamentId))
                    .orElseThrow(() -> new RuntimeException("Tournament not found"));
            tournament.setRegistrationStatus(RegistrationStatus.valueOf(registrationStatus.toUpperCase()));
            tournamentRepo.save(tournament);
            return VarList.Created;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getTournamentByStreamer(String email) {
        try {
            Streamer streamer = streamerRepo.getStreamersByEmail(email);
            return tournamentRepo.findAllByStreamer(streamer);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getTournamentById(String id) {
        try {
            return tournamentRepo.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("Tournament not found"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
