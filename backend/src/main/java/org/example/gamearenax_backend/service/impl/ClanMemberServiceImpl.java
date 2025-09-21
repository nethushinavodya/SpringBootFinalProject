package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.entity.*;
import org.example.gamearenax_backend.repository.ClanMemberRepo;
import org.example.gamearenax_backend.repository.ClanRepo;
import org.example.gamearenax_backend.repository.PlayerRepo;
import org.example.gamearenax_backend.repository.UserRepo;
import org.example.gamearenax_backend.service.ClanMemberService;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClanMemberServiceImpl implements ClanMemberService {
    private final ClanMemberRepo clanMemberRepository;
    private final PlayerRepo playerRepository;
    private final ClanRepo clanRepository;
    private final UserRepo userRepo;
    @Override
    public int joinClan(String playerId, String clanId) {
        try {
            UUID playerUUID = UUID.fromString(playerId.trim());

            System.out.println(playerId   + " service");
            Optional<Player> player = playerRepository.findByUserId(playerUUID);
            System.out.println(player.get().getAbout());
            Optional<Clan> clan = clanRepository.findById(UUID.fromString(clanId));

            ClanMember member = new ClanMember();
            member.setClan(clan.get());
            member.setPlayer(player.get());
            member.setJoinedAt(LocalDateTime.now());
            member.setRole(MemberRole.PLAYER);

            System.out.println("playerId: " + playerId);
            System.out.println("clanId: " + clanId);
            System.out.println("player: " + player.get());
            System.out.println("clan: " + clan.get());
            System.out.println();

            clanMemberRepository.save(member);

            clanRepository.updateAvailableSlots(UUID.fromString(clanId));
            return VarList.Created;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateRole(String playerId, String clanId, String role) {
        try {
            Optional<ClanMember> member = clanMemberRepository.findByPlayerIdAndClanId(UUID.fromString(playerId), UUID.fromString(clanId));
            member.get().setRole(MemberRole.valueOf(role.toUpperCase()));
            clanMemberRepository.save(member.get());
            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object leaveClan(String playerId, String clanId) {
        try {
            System.out.println(playerId + " playerId");
            System.out.println(clanId + " clanId");
            Optional<ClanMember> member = clanMemberRepository.findByPlayerIdAndClanId(UUID.fromString(playerId), UUID.fromString(clanId));
            clanMemberRepository.delete(member.get());
            clanRepository.increaseAvailableSlots(UUID.fromString(clanId));
            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getCurrentMember(String userName) {
        User user = userRepo.findByUsername(userName);
        Optional<Player> player = playerRepository.findByUserId(user.getUuid());
        Optional<ClanMember> member = clanMemberRepository.findByPlayer(player.get());
        return member;
    }
}
