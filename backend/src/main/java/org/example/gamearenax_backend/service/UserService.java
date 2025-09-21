package org.example.gamearenax_backend.service;

import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.entity.User;

import java.util.UUID;

public interface UserService {
    int saveUser(UserDTO userDTO);

    Object getAllUsers();

    int updateUser(UserDTO userDTO);

    User SearchByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Object getAllAdminsAndUsers();

    Object deleteUser(String email);

    Object activateUser(String email);

    UserDTO getUserByEmail(String email);

    UserDTO getUserByUsername(String username);
}
