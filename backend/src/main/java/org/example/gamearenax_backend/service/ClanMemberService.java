package org.example.gamearenax_backend.service;

public interface ClanMemberService {
    int joinClan(String playerId, String clanId);

    Object updateRole(String playerId, String clanId, String role);

    Object leaveClan(String playerId, String clanId);

    Object getCurrentMember(String userName);
}
