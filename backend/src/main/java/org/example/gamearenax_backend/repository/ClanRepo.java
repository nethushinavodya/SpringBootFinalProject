package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.Clan;
import org.example.gamearenax_backend.entity.ClanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClanRepo extends JpaRepository<Clan, UUID> {
    List<Clan> findByClanType(ClanType clanType);

    List<Clan> findAllByOrderByRankingPointsAsc();

    @Modifying
    @Query(value = "UPDATE Clans c SET c.available_slots = c.available_slots - 1 WHERE c.id = ?1", nativeQuery = true)
    void updateAvailableSlots(UUID uuid);

    @Modifying
    @Query(value = "UPDATE Clans c SET c.available_slots = c.available_slots + 1 WHERE c.id = ?1", nativeQuery = true)
    void increaseAvailableSlots(UUID uuid);

    Optional<Clan> findByName(String clanName);
}
