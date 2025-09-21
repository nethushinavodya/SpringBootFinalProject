package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.StreamerDTO;
import org.example.gamearenax_backend.entity.User;

public interface StreamerService {
    int addStreamer(StreamerDTO streamerDTO, User user);

    int updateStreamer(StreamerDTO streamerDTO);

    Object getAllStreamers();

    Object getStreamerByEmail(String email);

    Object deleteStreamer(String email);

    Object getStreamerByIsLive();

    Object updateIsLive(String email, String streamUrl);

    Object updateIsLiveFalse(String email);
}
