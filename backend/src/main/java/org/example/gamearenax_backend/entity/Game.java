package org.example.gamearenax_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gameId;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "genre")
    private String genre;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "platform")
    private String platform;
    @Column(name = "is_active")
    private boolean isActive = true;
}
