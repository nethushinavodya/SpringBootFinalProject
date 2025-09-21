package org.example.gamearenax_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StreamerDTO {
    private String displayName;
    private String email;
    private String bio;
    private String platform;
    private String streamUrl;
    private Integer followerCount;
    private String profileImageUrl;
    private String bannerImageUrl;
    private Boolean isLive;
    private Timestamp lastLiveAt;
    private List<String> featuredGames;
    private String country;
    private Timestamp joinedAt;
    private Boolean isVerified;

//    public StreamerDTO(String displayName,String email, String bio, String platform, String streamUrl, Integer followerCount, String profileImageUrl, String bannerImageUrl, Boolean isLive, Timestamp lastLiveAt, List<String> featuredGames, String country, Timestamp joinedAt, Boolean isVerified) {
//        this.displayName = displayName;
//        this.email = email;
//        this.bio = bio;
//        this.platform = platform;
//        this.streamUrl = streamUrl;
//        this.followerCount = followerCount;
//        this.profileImageUrl = profileImageUrl;
//        this.bannerImageUrl = bannerImageUrl;
//        this.isLive = isLive;
//        this.lastLiveAt = lastLiveAt;
//        this.featuredGames = featuredGames;
//        this.country = country;
//        this.joinedAt = joinedAt;
//        this.isVerified = isVerified;
//    }
}
