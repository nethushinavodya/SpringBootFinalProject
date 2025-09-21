package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.ClanDTO;

import java.util.List;

public interface ClanService {
    ClanDTO createClan(ClanDTO clanDTO);

    Object getAllClans();

    List<ClanDTO> getAllClansByOpen();

    List<ClanDTO> getAllClansByRankingPointsAsc();

    ClanDTO getClanById(String id);

    Object isJoinedClan(String username);

}
