package org.example.gamearenax_backend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Streamer")
public class Streamer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String displayName;
    private String email;
    @Column(length = 500)
    private String bio;

    private String platform;

    private String streamUrl;

    private Integer followerCount;

    private String profileImageUrl;

    private String bannerImageUrl;

    private Boolean isLive;

    private Timestamp lastLiveAt;

    @ElementCollection
    @CollectionTable(name = "streamer_games", joinColumns = @JoinColumn(name = "streamer_id"))
    private List<String> featuredGames;

    private String country;

    private Timestamp joinedAt;

    private Boolean isVerified;

    public Streamer(User user, String displayName,String email, String bio, String platform, String streamUrl, Integer followerCount, String profileImageUrl, String bannerImageUrl, Boolean isLive, Timestamp lastLiveAt, List<String> featuredGames, String country, Timestamp joinedAt, Boolean isVerified) {
        this.user = user;
        this.displayName = displayName;
        this.email = email;
        this.bio = bio;
        this.platform = platform;
        this.streamUrl = streamUrl;
        this.followerCount = followerCount;
        this.profileImageUrl = profileImageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.isLive = isLive;
        this.lastLiveAt = lastLiveAt;
        this.featuredGames = featuredGames;
        this.country = country;
        this.joinedAt = joinedAt;
        this.isVerified = isVerified;
    }
}
