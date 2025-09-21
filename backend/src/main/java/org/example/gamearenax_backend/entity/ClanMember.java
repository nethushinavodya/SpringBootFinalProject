package org.example.gamearenax_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clan_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClanMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "clan_id", nullable = false)
    private Clan clan;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    private Player player;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role = MemberRole.PLAYER; // LEADER, CO_LEADER, PLAYER, STREAMER

    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();
}
