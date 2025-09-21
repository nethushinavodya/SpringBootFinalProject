package org.example.gamearenax_backend.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.dto.PlayerDTO;
import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.entity.Player;
import org.example.gamearenax_backend.entity.User;
import org.example.gamearenax_backend.repository.PlayerRepo;
import org.example.gamearenax_backend.repository.UserRepo;
import org.example.gamearenax_backend.service.PlayerService;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @PostConstruct
    public void configureMapper() {
        modelMapper.typeMap(PlayerDTO.class, Player.class)
                .addMappings(mapper -> mapper.map(PlayerDTO::isOnline, Player::setIsOnline));

        modelMapper.typeMap(Player.class, PlayerDTO.class)
                .addMappings(mapper -> mapper.map(Player::getIsOnline, PlayerDTO::setOnline));
    }

    @Autowired
    private PlayerRepo playerRepo;
    @Override
    public int addPlayer(PlayerDTO playerDTO, User user) {
        try {
            System.out.println("isOnline: " + playerDTO.isOnline());
            if(userRepo.existsByEmail(playerDTO.getEmail())) {
                Player player = modelMapper.map(playerDTO, Player.class);
                player.setUser(user);
                player.setIsOnline(playerDTO.isOnline());
                playerRepo.save(player);
                userRepo.updatePlayerRole(playerDTO.getEmail(), "Player");
                userRepo.updateCountry(playerDTO.getEmail(), playerDTO.getCountry());
                userRepo.updateProfilePicture(playerDTO.getEmail(), playerDTO.getImageUrl());
                return VarList.Created;
            } else {
                return VarList.Not_Acceptable;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Object getAllPlayers() {
        try {
            return playerRepo.findAll();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getByOnline() {
        try {
            List<Player> players = playerRepo.findByIsOnline(true);
            if (players.isEmpty()) {
                return "No players are online";
            } else {
                return players;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public int updatePlayer(PlayerDTO playerDTO) {
        try {
            playerRepo.updatePlayer(playerDTO.getPlayerName(), playerDTO.getCountry(), playerDTO.getAbout(),playerDTO.getImageUrl(), playerDTO.getEmail());
            userRepo.updateCountry(playerDTO.getEmail(), playerDTO.getCountry());
            userRepo.updateImgUrl(playerDTO.getEmail(), playerDTO.getImageUrl());
            return VarList.Created;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateIsLive(String email) {
        try {
            if (playerRepo.existsByEmail(email)){
                playerRepo.updateIsLive(email);
                return VarList.Created;
            }else {
                return "Player Not Found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateIsLiveFalse(String email) {
        try {
            if (playerRepo.existsByEmail(email)){
                playerRepo.updateIsLiveFalse(email);
                return VarList.Created;
            }else {
                return "Player Not Found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object banPlayer(String email) {
        try {
            if (playerRepo.existsByEmail(email)){
                playerRepo.updateStatus(email);
                return VarList.Created;
            }else {
                return "Player Not Found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object unbanPlayer(String email) {
        try {
            if (playerRepo.existsByEmail(email)){
                playerRepo.updateStatusActive(email);
                return VarList.Created;
            }else {
                return "Player Not Found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getPlayerByUsername(String username) {
        try {
            List<Player> players = playerRepo.findByUserUsername(username);
            return players.stream()
                    .map(player -> {
                        PlayerDTO dto = modelMapper.map(player, PlayerDTO.class);
                        dto.setIsOnline(player.getIsOnline()); // ✅ force correct value
                        return dto;
                    })
                    .toList();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PlayerDTO getPlayerByEmail(String email) {
        Player player = playerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Map Player entity to PlayerDTO
        PlayerDTO dto = modelMapper.map(player, PlayerDTO.class);
        return dto;
    }

}
