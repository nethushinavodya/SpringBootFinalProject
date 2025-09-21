package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.PlayerDTO;
import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.entity.User;

public interface PlayerService {
    int addPlayer(PlayerDTO playerDTO, User user);

    Object getAllPlayers();

    Object getByOnline();


    int updatePlayer(PlayerDTO playerDTO);

    Object updateIsLive(String email);

    Object updateIsLiveFalse(String email);

    Object banPlayer(String email);

    Object unbanPlayer(String email);

    Object getPlayerByUsername(String username);

    PlayerDTO getPlayerByEmail(String email);
}
