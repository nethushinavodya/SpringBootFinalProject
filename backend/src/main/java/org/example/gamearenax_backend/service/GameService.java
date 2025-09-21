package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.GameDTO;
import org.example.gamearenax_backend.entity.Game;

public interface GameService {
    int saveGame(GameDTO gameDTO);

    Object getAllGames();

    int updateGame(GameDTO gameDTO);  

    int deleteGame(String name);

    Game getGameByName(String name);

    Object getGameByActive();

    int setActiveTrue(String name);
}
