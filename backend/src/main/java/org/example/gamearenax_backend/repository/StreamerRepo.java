package org.example.gamearenax_backend.repository;

import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.entity.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamerRepo extends JpaRepository<Streamer, String> {
    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE Streamer s SET s.country = ?1, s.bio = ?2,s.display_name = ?3, s.platform = ?4, s.stream_url = ?5, s.banner_image_url = ?6, s.profile_image_url = ?7 WHERE s.email = ?8", nativeQuery = true)
    void updateStreamer(String country, String bio, String displayName, String platform, String streamUrl,String bannerImageUrl,String profileImageUrl, String email);

    Object findByEmail(String email);

    List<Streamer> findByIsLive(boolean b);

    @Modifying
    @Query(value = "UPDATE Streamer s SET s.is_live = true, s.stream_url = ?2 WHERE s.email = ?1", nativeQuery = true)
    void updateIsLive(String email, String streamUrl);

    @Modifying
    @Query(value = "UPDATE Streamer s SET s.is_live = false WHERE s.email = ?1", nativeQuery = true)
    void updateIsLiveFalse(String email);

    Streamer getStreamersByEmail(String streamerEmail);
}
