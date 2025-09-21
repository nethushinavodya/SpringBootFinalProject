package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepo extends JpaRepository<Game, String> {
    boolean existsByName(String name);

    Game findByName(String name);

    void deleteByName(String name);

    @Modifying
    @Query(value = "UPDATE Game g SET g.is_active = false WHERE g.name = :name",nativeQuery = true)
    void updateIsActive(String name);


    List<Game> findByIsActive(boolean b);

    @Modifying
    @Query(value = "UPDATE Game g SET g.is_active = true WHERE g.name = :name",nativeQuery = true)
    void updateIsActiveTrue(String name);
}
