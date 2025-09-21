package org.example.gamearenax_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.ClanDTO;
import org.example.gamearenax_backend.dto.ClanMemberDTO;
import org.example.gamearenax_backend.entity.*;
import org.example.gamearenax_backend.repository.*;
import org.example.gamearenax_backend.service.ClanService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements ClanService {

    private final ClanRepo clanRepository;
    private final ClanMemberRepo clanMemberRepository;
    private final UserRepo userRepository;
    private final PlayerRepo playerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ClanDTO createClan(ClanDTO dto) {
        Player leader = playerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Player not found: " + dto.getEmail()));

        Clan clan = modelMapper.map(dto, Clan.class);
        clan.setLeader(leader);
        clan.setClanType(ClanType.valueOf(dto.getClanType().toUpperCase()));
        clan.setMemberLimit(dto.getMemberLimit() > 0 ? dto.getMemberLimit() : 50);
        clan.setAvailableSlots(clan.getMemberLimit() - 1);
        clan.setCreatedAt(LocalDateTime.now());
        clan.setEmail(leader.getEmail());

        clanRepository.save(clan);
        ClanMember leaderMember = ClanMember.builder()
                .clan(clan)
                .player(leader)
                .role(MemberRole.LEADER)
                .joinedAt(LocalDateTime.now())
                .build();

        clanMemberRepository.save(leaderMember);
        if (clan.getMembers() == null) {
            clan.setMembers(new ArrayList<>());
        }
        clan.getMembers().add(leaderMember);
        ClanDTO resultDto = modelMapper.map(clan, ClanDTO.class);
        resultDto.setEmail(leader.getEmail());

        if (clan.getMembers() != null) {
            resultDto.setMembers(clan.getMembers().stream()
                    .map(m -> ClanMemberDTO.builder()
                            .clanId(m.getClan().getId())
                            .playerId(m.getPlayer().getPlayerId())
                            .role(m.getRole().name())
                            .build())
                    .toList());
        }

        return resultDto;
    }

    @Override
    public Object getAllClans() {
        return clanRepository.findAll();

    }

    @Override
    public List<ClanDTO> getAllClansByOpen() {
        List<Clan> clans = clanRepository.findByClanType(ClanType.OPEN);
        return clans.stream()
                .map(clan -> modelMapper.map(clan, ClanDTO.class))
                .toList();
    }

    @Override
    public List<ClanDTO> getAllClansByRankingPointsAsc() {
        List<Clan> clans = clanRepository.findAllByOrderByRankingPointsAsc();
        return clans.stream()
                .map(clan -> modelMapper.map(clan, ClanDTO.class))
                .toList();
    }

    @Override
    public ClanDTO getClanById(String id) {
        try {
            UUID uuid = UUID.fromString(id.trim());
            Clan clan = clanRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("Clan not found: " + id));
            return modelMapper.map(clan, ClanDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object isJoinedClan(String username) {
        User user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.getUuid());
        Optional<ClanMember> clanMember = clanMemberRepository.findByPlayer(player.get());
        return clanMember;
    }

}