package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);



    @Modifying
    @Query(value = "UPDATE User u SET u.role = 'Streamer' WHERE u.email = ?1", nativeQuery = true)
    void updateRole(String email, String streamer);

    @Modifying
    @Query(value = "UPDATE User u SET u.status = 'Deactivated' WHERE u.email = ?1", nativeQuery = true)
    void updateIsActive(String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.role = 'Player' WHERE u.email = ?1", nativeQuery = true)
    void updatePlayerRole(String email, String player);

    @Modifying
    @Query(value = "UPDATE User u SET u.password = ?1 WHERE u.email = ?2", nativeQuery = true)
    void updatePassword(String encode, String email);

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM User u WHERE u.role = 'Admin' OR u.role = 'User'", nativeQuery = true)
    List<User> getAllAdminsAndUsers();

    @Modifying
    @Query(value = "DELETE FROM User u WHERE u.email = ?1", nativeQuery = true)
    void deleteByEmail(String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.status = 'Active' WHERE u.email = ?1", nativeQuery = true)
    void updateActiveUser(String email);

    User findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE User u SET u.country = ?2 WHERE u.email = ?1", nativeQuery = true)
    void updateCountry(String email, String country);

    @Modifying
    @Query(value = "UPDATE User u SET u.profile_picture = ?2 WHERE u.email = ?1", nativeQuery = true)
    void updateImgUrl(String email, String imageUrl);

    @Modifying
    @Query(value = "UPDATE User u SET u.email = ?1, u.country = ?2, u.profile_picture = ?3 WHERE u.username = ?4", nativeQuery = true)
    void updateUser(String email, String country, String profilePicture, String username);

    @Modifying
    @Query(value = "UPDATE User u SET u.profile_picture = ?2 WHERE u.email = ?1", nativeQuery = true)
    void updateProfilePicture(String email, String imageUrl);
}
