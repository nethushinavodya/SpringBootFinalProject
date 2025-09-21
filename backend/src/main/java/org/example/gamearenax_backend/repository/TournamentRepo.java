package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.Streamer;
import org.example.gamearenax_backend.entity.Tournament;
import org.example.gamearenax_backend.entity.TournamentStatus;
import org.example.gamearenax_backend.entity.TournamentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TournamentRepo extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStatus(TournamentStatus tournamentStatus);

    List<Tournament> findByType(TournamentType tournamentType);

    List<Tournament> findAllByStreamer(Streamer streamer);
}
