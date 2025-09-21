package org.example.gamearenax_backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gamearenax_backend.dto.BracketsDTO;
import org.example.gamearenax_backend.dto.TournamentParticipantDTO;
import org.example.gamearenax_backend.entity.Brackets;
import org.example.gamearenax_backend.entity.TournamentParticipant;
import org.example.gamearenax_backend.entity.MatchStatus;
import org.example.gamearenax_backend.repository.BracketsRepository;
import org.example.gamearenax_backend.repository.TournamentParticipantRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BracketService {

    private final BracketsRepository bracketsRepository;
    private final TournamentParticipantRepo participantRepository;

    @Transactional
    public List<BracketsDTO> generateBracket(Long tournamentId) {
        log.info("Generating bracket for tournament: {}", tournamentId);

        // Check if bracket already exists
        if (bracketsRepository.existsByTournamentId(tournamentId)) {
            throw new IllegalStateException("Bracket already exists for tournament: " + tournamentId);
        }

        // Get all participants for the tournament
        List<TournamentParticipant> participants = participantRepository.findByTournamentId(tournamentId);

        if (participants.isEmpty()) {
            throw new IllegalStateException("No participants found for tournament: " + tournamentId);
        }

        // Shuffle participants for random seeding
        Collections.shuffle(participants);

        // Calculate tournament structure
        int participantCount = participants.size();
        int nextPowerOfTwo = getNextPowerOfTwo(participantCount);
        int totalRounds = (int) (Math.log(nextPowerOfTwo) / Math.log(2));

        log.info("Tournament structure - Participants: {}, Next Power of 2: {}, Total Rounds: {}",
                participantCount, nextPowerOfTwo, totalRounds);

        // Generate first round matches
        List<Brackets> firstRoundMatches = generateFirstRound(tournamentId, participants, nextPowerOfTwo);

        // Generate subsequent rounds (empty matches)
        List<Brackets> allMatches = new ArrayList<>(firstRoundMatches);
        allMatches.addAll(generateSubsequentRounds(tournamentId, nextPowerOfTwo, totalRounds));

        // Save all matches
        List<Brackets> savedBrackets = bracketsRepository.saveAll(allMatches);

        return savedBrackets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private List<Brackets> generateFirstRound(Long tournamentId, List<TournamentParticipant> participants, int nextPowerOfTwo) {
        List<Brackets> matches = new ArrayList<>();
        int matchNumber = 1;
        int participantIndex = 0;

        for (int i = 0; i < nextPowerOfTwo / 2; i++) {
            Brackets match = Brackets.builder()
                    .tournamentId(tournamentId)
                    .round(1)
                    .matchNumber(matchNumber++)
                    .status(MatchStatus.PENDING)
                    .build();

            // Assign participants if available
            if (participantIndex < participants.size()) {
                match.setPlayerA(participants.get(participantIndex++));
            }
            if (participantIndex < participants.size()) {
                match.setPlayerB(participants.get(participantIndex++));
            }

            // If only one player in a match, they automatically advance
            if (match.getPlayerA() != null && match.getPlayerB() == null) {
                match.setWinner(match.getPlayerA());
                match.setStatus(MatchStatus.COMPLETED);
            }

            matches.add(match);
        }

        return matches;
    }

    private List<Brackets> generateSubsequentRounds(Long tournamentId, int nextPowerOfTwo, int totalRounds) {
        List<Brackets> matches = new ArrayList<>();

        for (int round = 2; round <= totalRounds; round++) {
            int matchesInRound = nextPowerOfTwo / (int) Math.pow(2, round);

            for (int match = 1; match <= matchesInRound; match++) {
                Brackets bracket = Brackets.builder()
                        .tournamentId(tournamentId)
                        .round(round)
                        .matchNumber(match)
                        .status(MatchStatus.PENDING)
                        .build();

                matches.add(bracket);
            }
        }

        return matches;
    }

    public List<BracketsDTO> getTournamentBracket(Long tournamentId) {
        List<Brackets> brackets = bracketsRepository.findByTournamentIdOrderByRoundAscMatchNumberAsc(tournamentId);

        return brackets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BracketsDTO updateMatchResult(Long matchId, Long winnerId) {
        Brackets match = bracketsRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        // Validate winner
        TournamentParticipant winner = null;
        if (match.getPlayerA() != null && match.getPlayerA().getId().equals(winnerId)) {
            winner = match.getPlayerA();
        } else if (match.getPlayerB() != null && match.getPlayerB().getId().equals(winnerId)) {
            winner = match.getPlayerB();
        } else {
            throw new IllegalArgumentException("Winner must be one of the match participants");
        }

        match.setWinner(winner);
        match.setStatus(MatchStatus.COMPLETED);

        // Advance winner to next round
        advanceWinnerToNextRound(match, winner);

        Brackets savedMatch = bracketsRepository.save(match);
        return convertToDTO(savedMatch);
    }

    private void advanceWinnerToNextRound(Brackets currentMatch, TournamentParticipant winner) {
        int nextRound = currentMatch.getRound() + 1;
        int nextMatchNumber = (currentMatch.getMatchNumber() + 1) / 2;

        List<Brackets> nextRoundMatches = bracketsRepository.findByTournamentIdAndRound(
                currentMatch.getTournamentId(), nextRound);

        Optional<Brackets> nextMatch = nextRoundMatches.stream()
                .filter(m -> m.getMatchNumber() == nextMatchNumber)
                .findFirst();

        if (nextMatch.isPresent()) {
            Brackets match = nextMatch.get();

            if (match.getPlayerA() == null) {
                match.setPlayerA(winner);
            } else if (match.getPlayerB() == null) {
                match.setPlayerB(winner);
            }

            bracketsRepository.save(match);
        }
    }

    private int getNextPowerOfTwo(int n) {
        if (n <= 1) return 2;
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }

    private BracketsDTO convertToDTO(Brackets bracket) {
        return BracketsDTO.builder()
                .id(bracket.getId())
                .tournamentId(bracket.getTournamentId())
                .round(bracket.getRound())
                .matchNumber(bracket.getMatchNumber())
                .playerA(bracket.getPlayerA() != null ? convertParticipantToDTO(bracket.getPlayerA()) : null)
                .playerB(bracket.getPlayerB() != null ? convertParticipantToDTO(bracket.getPlayerB()) : null)
                .winner(bracket.getWinner() != null ? convertParticipantToDTO(bracket.getWinner()) : null)
                .status(bracket.getStatus())
                .build();
    }

    private TournamentParticipantDTO convertParticipantToDTO(TournamentParticipant participant) {
        return TournamentParticipantDTO.builder()
                .id(participant.getId())
                .tournamentId(participant.getTournament().getId())
                .clanId(participant.getClan() != null ? String.valueOf(participant.getClan().getId()) : null)
                .clanName(participant.getClan() != null ? participant.getClan().getName() : null)
                .playerId(participant.getPlayer() != null ? String.valueOf(participant.getPlayer().getPlayerId()) : null)
                //.playerName(participant.getPlayer() != null ? participant.getPlayer().getPlayerName() : null)
                //.registeredAt(participant.getRegisteredAt().toString())
                .build();
    }
}