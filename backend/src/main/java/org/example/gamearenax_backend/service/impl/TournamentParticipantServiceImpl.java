package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.TournamentParticipantDTO;
import org.example.gamearenax_backend.entity.*;
import org.example.gamearenax_backend.repository.ClanRepo;
import org.example.gamearenax_backend.repository.PlayerRepo;
import org.example.gamearenax_backend.repository.TournamentParticipantRepo;
import org.example.gamearenax_backend.repository.TournamentRepo;
import org.example.gamearenax_backend.service.TournamentParticipantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TournamentParticipantServiceImpl implements TournamentParticipantService {

    private final TournamentParticipantRepo tournamentParticipantRepo;
    private final TournamentRepo tournamentRepo;
    private final PlayerRepo playerRepo;
    private final ClanRepo clanRepo;
    private final ModelMapper modelMapper;

    @Override
    public TournamentParticipantDTO joinTournament(TournamentParticipantDTO dto) {
        Tournament tournament = tournamentRepo.findById(dto.getTournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (!(tournament.getStatus() == TournamentStatus.ONGOING
                || (tournament.getStatus() == TournamentStatus.UPCOMING
                && tournament.getRegistrationStatus() == RegistrationStatus.OPEN))) {
            throw new RuntimeException("Tournament is not open for registration");
        }

        if (tournament.getRegistrationStatus() == RegistrationStatus.CLOSED) {
            throw new RuntimeException("Tournament registration is closed");
        }

        Clan clan = null;
        if (dto.getClanName() != null && !dto.getClanName().isEmpty()) {
            clan = clanRepo.findByName(dto.getClanName())
                    .orElseThrow(() -> new RuntimeException("Clan not found"));
        }

        Player player = null;
        if (dto.getPlayerEmail() != null && !dto.getPlayerEmail().isEmpty()) {
            player = playerRepo.findByEmail(dto.getPlayerEmail())
                    .orElseThrow(() -> new RuntimeException("Player not found"));
        }

        if (tournament.getType() == TournamentType.CLAN && player != null) {
            throw new RuntimeException("Only clans can join this tournament");
        }
        if (tournament.getType() == TournamentType.SOLO && clan != null) {
            throw new RuntimeException("Only players can join this tournament");
        }

        if (clan != null && tournamentParticipantRepo.existsByTournamentAndClan(tournament, clan)) {
            throw new RuntimeException("Clan is already registered for this tournament");
        }
        if (player != null && tournamentParticipantRepo.existsByTournamentAndPlayer(tournament, player)) {
            throw new RuntimeException("Player is already registered for this tournament");
        }

        long currentCount = tournamentParticipantRepo.countByTournament(tournament);
        int remainingSlots = tournament.getMaxParticipants() - (int) currentCount - 1;
        if (remainingSlots < 0) {
            tournament.setRegistrationStatus(RegistrationStatus.CLOSED);
            tournamentRepo.save(tournament);
            throw new RuntimeException("Tournament is full. Registration closed.");
        }

        TournamentParticipant participant = TournamentParticipant.builder()
                .tournament(tournament)
                .clan(clan)
                .player(player)
                .build();

        tournamentParticipantRepo.save(participant);

        long updatedCount = tournamentParticipantRepo.countByTournament(tournament);
        if (updatedCount >= tournament.getMaxParticipants()) {
            tournament.setRegistrationStatus(RegistrationStatus.CLOSED);
            tournamentRepo.save(tournament);
        }

        TournamentParticipantDTO response = modelMapper.map(participant, TournamentParticipantDTO.class);
        response.setRemainingSlots(remainingSlots); 
        return response;
    }

    @Override
    public Object getParticipantsByTournamentId(String tournamentId) {
        try {
           List<TournamentParticipant> tournamentParticipants = tournamentParticipantRepo.findByTournamentId(Long.parseLong(tournamentId));
           return tournamentParticipants.stream().map(participant -> modelMapper.map(participant, TournamentParticipantDTO.class)).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getTournamentsByPlayerEmail(String playerEmail) {
        try {
            // Fetch all participant entries for the given player
            List<TournamentParticipant> participants = tournamentParticipantRepo.findByPlayerEmail(playerEmail);

            // Extract only the tournament IDs
            List<Long> tournamentIds = participants.stream()
                    .map(participant -> participant.getTournament().getId())
                    .toList();

            return tournamentIds;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching tournaments: " + e.getMessage());
        }
    }



}
