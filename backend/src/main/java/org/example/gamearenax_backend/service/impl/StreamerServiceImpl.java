package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.dto.StreamerDTO;
import org.example.gamearenax_backend.entity.Streamer;
import org.example.gamearenax_backend.entity.User;
import org.example.gamearenax_backend.repository.StreamerRepo;
import org.example.gamearenax_backend.repository.UserRepo;
import org.example.gamearenax_backend.service.StreamerService;
import org.example.gamearenax_backend.service.UserService;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StreamerServiceImpl implements StreamerService {
    @Autowired
    private StreamerRepo streamerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Override

    public int addStreamer(StreamerDTO streamerDTO, User user) {
        try {
            if (userRepo.existsByEmail(streamerDTO.getEmail())){
                Streamer streamer = modelMapper.map(streamerDTO, Streamer.class);
                streamer.setUser(user);
                streamerRepo.save(streamer);
                System.out.println("email: " + streamerDTO.getEmail());
                userRepo.updateRole(streamerDTO.getEmail(), "Streamer");

                return VarList.Created;

            }else {
                return VarList.Not_Acceptable;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int updateStreamer(StreamerDTO streamerDTO) {
        try {
            streamerRepo.updateStreamer(streamerDTO.getCountry(),streamerDTO.getBio(),streamerDTO.getDisplayName(),streamerDTO.getPlatform(),streamerDTO.getStreamUrl(),streamerDTO.getBannerImageUrl(),streamerDTO.getProfileImageUrl(),streamerDTO.getEmail());
            return VarList.Created;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getAllStreamers() {
        try {
            return streamerRepo.findAll();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getStreamerByEmail(String email) {
        try {
            if (streamerRepo.existsByEmail(email)){
                return streamerRepo.findByEmail(email);
            }else {
                return "Streamer not found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object deleteStreamer(String email) {
        try {
            if (streamerRepo.existsByEmail(email)){
                userRepo.updateIsActive(email);
                return VarList.Created;
            }else {
                return "Streamer not found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getStreamerByIsLive() {
        try {
            List<Streamer> streamers = streamerRepo.findByIsLive(true);
            if (streamers.isEmpty()){
                return "No streamers are live";
            }else {
                return streamers;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateIsLive(String email, String streamUrl) {
        try {
            if (streamerRepo.existsByEmail(email)){
                streamerRepo.updateIsLive(email, streamUrl);
                return VarList.Created;
            }else {
                return "Streamer not found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object updateIsLiveFalse(String email) {
        try{
            if (streamerRepo.existsByEmail(email)){
                streamerRepo.updateIsLiveFalse(email);
                return VarList.Created;
            }else {
                return "Streamer not found";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
