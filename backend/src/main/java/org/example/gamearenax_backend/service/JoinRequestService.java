package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.JoinRequestDTO;

import java.util.List;

public interface JoinRequestService {
    List<Object> getAllJoinRequests();

    int createJoinRequest(JoinRequestDTO joinRequestDTO);

    int acceptJoinRequest(String id);

    Object deleteJoinRequest(String id);

    Object getJoinRequestByClanId(String clanId);

}
