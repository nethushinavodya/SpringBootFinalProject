package org.example.gamearenax_backend.repository;

import org.example.gamearenax_backend.entity.JoinRequest;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JoinRequestRepo extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByClanId(UUID uuid);

}
