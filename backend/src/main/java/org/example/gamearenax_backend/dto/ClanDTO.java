package org.example.gamearenax_backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClanDTO {
    private String name;
    private String description;
    private String email;
    private String clanLogoUrl;
    private String bannerUrl;
    private int memberLimit;
    private  int availableSlots;
    private String clanType;  // ClanType as String
    private int rankingPoints;
    private LocalDateTime createdAt;
    private List<ClanMemberDTO> members;
}
