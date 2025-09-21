package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.JoinRequestDTO;
import org.example.gamearenax_backend.entity.*;
import org.example.gamearenax_backend.repository.ClanMemberRepo;
import org.example.gamearenax_backend.repository.ClanRepo;
import org.example.gamearenax_backend.repository.JoinRequestRepo;
import org.example.gamearenax_backend.repository.PlayerRepo;
import org.example.gamearenax_backend.service.JoinRequestService;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinRequestServiceImpl implements JoinRequestService {

    private final JoinRequestRepo joinRequestRepo;
    private final PlayerRepo playerRepo;
    private final ClanRepo clanRepo;
    private final ClanMemberRepo clanMemberRepo;

    private final ModelMapper modelMapper;

    @Override
    public List<Object> getAllJoinRequests() {
        List<JoinRequest> joinRequests = joinRequestRepo.findAll();
        return Collections.singletonList(joinRequests.stream().map(joinRequest -> modelMapper.map(joinRequest, JoinRequestDTO.class)).toList());
    }

    @Override
    public int createJoinRequest(JoinRequestDTO joinRequestDTO) {
        try {
            Optional<Player> player = playerRepo.findByUserId(UUID.fromString(joinRequestDTO.getPlayerId()));
            Optional<Clan> clan = clanRepo.findById(UUID.fromString(joinRequestDTO.getClanId()));
            System.out.println(player.get().getPlayerId() + " playerID");
            System.out.println(clan.get().getId() + " clanID");

            JoinRequest joinRequest = new JoinRequest();
            joinRequest.setPlayer(player.get());
            joinRequest.setClan(clan.get());
            joinRequest.setMessage(joinRequestDTO.getMessage());
            joinRequest.setStatus(joinRequestDTO.getStatus());

            System.out.println(joinRequest + " joinRequest");
            joinRequestRepo.save(joinRequest);
            return VarList.Created;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int acceptJoinRequest(String id) {
        Optional<JoinRequest> joinRequest = joinRequestRepo.findById(Long.valueOf(id));
        System.out.println("joinRequest: " + joinRequest);
        if (joinRequest.isPresent()) {
            joinRequestRepo.delete(joinRequest.get());

            ClanMember clanMember = new ClanMember();
            clanMember.setClan(joinRequest.get().getClan());
            clanMember.setPlayer(joinRequest.get().getPlayer());
            clanMember.setJoinedAt(LocalDateTime.now());
            clanMember.setRole(MemberRole.PLAYER);

            clanMemberRepo.save(clanMember);

            //final
            clanRepo.updateAvailableSlots(joinRequest.get().getClan().getId());

            return VarList.Created;
        } else {
            return VarList.Not_Acceptable;
        }


    }

    @Override
    public Object deleteJoinRequest(String id) {
        try {
            joinRequestRepo.deleteById(Long.valueOf(id));
            return VarList.OK;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<JoinRequest> getJoinRequestByClanId(String clanId) {
        try {
            UUID clanUUID = UUID.fromString(clanId.trim());
            System.out.println(clanUUID + "clanUUID");
            return joinRequestRepo.findByClanId(clanUUID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

