package org.example.gamearenax_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TournamentDTO {
    private Long id;
    private String name;
    private String description;
    private String gameName;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal prizePool;

    private String status;
    private String type;
    private int maxParticipants;

    private String StreamerEmail;


    public TournamentDTO(Long id, String name, String description, String gameName, LocalDate startDate, LocalDate endDate, BigDecimal prizePool, String status, String type, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameName = gameName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prizePool = prizePool;
        this.status = status;
        this.type = type;
        this.maxParticipants = maxParticipants;
    }
}
