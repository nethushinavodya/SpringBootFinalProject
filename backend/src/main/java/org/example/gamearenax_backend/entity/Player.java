package org.example.gamearenax_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID playerId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String email;
    private String playerName;
    private String role;
    private String country;

    private String imageUrl;
    private boolean isOnline;

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Column(length = 1000)
    private String about;

    private int totalMatches;
    private int wins;
    @Column(name = "`rank`")
    private String rank;

    private String status;

    @ElementCollection
    @CollectionTable(name = "player_game", joinColumns = @JoinColumn(name = "player_id"))
    private List<String> games;

    public boolean getIsOnline() {
        return isOnline;
    }
}
