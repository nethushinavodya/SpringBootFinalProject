package org.example.gamearenax_backend.repository;

import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.entity.ClanMember;
import org.example.gamearenax_backend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClanMemberRepo extends JpaRepository<ClanMember, UUID> {
    @Transactional
    @Query
    (value = "SELECT * FROM clan_members WHERE player_id = ?1 AND clan_id = ?2", nativeQuery = true)
    Optional<ClanMember> findByPlayerIdAndClanId(UUID uuid, UUID uuid1);

    Optional<ClanMember> findByPlayer(Player player);
}
