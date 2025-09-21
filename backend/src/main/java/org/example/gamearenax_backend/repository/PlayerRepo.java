package org.example.gamearenax_backend.repository;

import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.entity.Player;
import org.example.gamearenax_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepo extends JpaRepository<Player, UUID> {
    List<Player> findByIsOnline(boolean isOnline);

    Optional<Player> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Player p SET p.player_name = ?1, p.country = ?2, p.about = ?3, p.image_url = ?4 WHERE p.email = ?5", nativeQuery = true)
    void updatePlayer(String playerName, String country, String about,String imageUrl, String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE Player s SET s.is_online  = true WHERE s.email = ?1", nativeQuery = true)
    void updateIsLive(String email);

    @Modifying
    @Query(value = "UPDATE Player s SET s.is_online  = false WHERE s.email = ?1", nativeQuery = true)
    void updateIsLiveFalse(String email);

    @Modifying
    @Query(value = "UPDATE Player s SET s.status = 'Deactivated' WHERE s.email = ?1", nativeQuery = true)
    void updateStatus(String email);

    @Modifying
    @Query(value = "UPDATE Player s SET s.status = 'Active' WHERE s.email = ?1", nativeQuery = true)
    void updateStatusActive(String email);

    List<Player> findByUserUsername(String username);

    @Query(value = "SELECT * FROM Player p WHERE p.user_id = ?1", nativeQuery = true)
    Optional<Player> findByUserId(UUID uuid);
}
