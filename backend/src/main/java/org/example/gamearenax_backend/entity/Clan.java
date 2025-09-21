package org.example.gamearenax_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String email;

/*
    @JsonIgnore
*/
    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private Player leader;

    @Column(name = "clan_logo_url")
    private String clanLogoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "member_limit")
    private int memberLimit = 50;

    @Column(name = "available_slots")
    private int availableSlots;

    @Enumerated(EnumType.STRING)
    @Column(name = "clan_type", nullable = false)
    private ClanType clanType; // OPEN, CLOSED, REQUEST

    @Column(name = "ranking_points")
    private int rankingPoints = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    private List<ClanMember> members;
}
