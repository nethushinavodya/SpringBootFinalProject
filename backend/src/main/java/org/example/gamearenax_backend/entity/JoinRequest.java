package org.example.gamearenax_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Clan clan;
    private String message; 
    private String status;

    public JoinRequest(Player player, Clan clan, String message, String status) {
        this.player = player;
        this.clan = clan;
        this.message = message;
        this.status = status;
    }
}
