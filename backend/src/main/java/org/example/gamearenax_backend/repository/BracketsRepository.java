
// Repository for Brackets
package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.Brackets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BracketsRepository extends JpaRepository<Brackets, Long> {
    List<Brackets> findByTournamentIdOrderByRoundAscMatchNumberAsc(Long tournamentId);

    List<Brackets> findByTournamentIdAndRound(Long tournamentId, int round);

    @Query("SELECT MAX(b.round) FROM Brackets b WHERE b.tournamentId = :tournamentId")
    Integer getMaxRoundByTournamentId(@Param("tournamentId") Long tournamentId);

    boolean existsByTournamentId(Long tournamentId);
}