package org.example.gamearenax_backend.service.impl;

import jakarta.transaction.Transactional;
import org.example.gamearenax_backend.dto.OTPDetails;
import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.entity.User;
import org.example.gamearenax_backend.repository.UserRepo;
import org.example.gamearenax_backend.service.UserService;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private UserRepo userRepo;

    private ConcurrentHashMap<String, OTPDetails> otpMap = new ConcurrentHashMap<>();

    @Autowired
    private ModelMapper modelMapper;
    public int saveUser(UserDTO userDTO) {
        if (userRepo.existsByEmail(userDTO.getEmail())){
            return VarList.Not_Acceptable;
        }
        if (userRepo.existsByUsername(userDTO.getUsername())) {
            return VarList.Conflict;
        }
        else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepo.save(modelMapper.map(userDTO, User.class));
            return VarList.Created;
        }
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email + " lllllllll");
        User user = userRepo.findByEmail(email);
        System.out.println(user + " lllllllll");
        System.out.println(user.getRole() + "hhhhh");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        System.out.println(user.getRole());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return modelMapper.map(users,new TypeToken<List<User>>(){}.getType());
    }

    @Override
    public int updateUser(UserDTO userDTO) {
       try {
           System.out.println(userDTO.getUsername() + " " + userDTO.getCountry() + " " + userDTO.getRole());
           userRepo.updateUser(userDTO.getEmail(), userDTO.getCountry(),userDTO.getProfilePicture(),userDTO.getUsername());
           return VarList.Created;
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }

    @Override
    public User SearchByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Object getAllAdminsAndUsers() {
        List<User> users = userRepo.getAllAdminsAndUsers();
        return modelMapper.map(users,new TypeToken<List<User>>(){}.getType());
    }

    @Override
    public Object deleteUser(String email) {
        try {
            if (userRepo.existsByEmail(email)){
                userRepo.updateIsActive(email);
                return VarList.Created;
            }else {
                return "User not found";
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object activateUser(String email) {
        try {
            if (userRepo.existsByEmail(email)){
                userRepo.updateActiveUser(email);
                return VarList.Created;
            }else {
                return "User not found";
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try {
            User user = userRepo.findByEmail(email);
            return modelMapper.map(user,UserDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        try {
            User user = userRepo.findByUsername(username);
            return modelMapper.map(user,UserDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean ifEmailExists(String email) {
        return userRepo.existsByEmail(email);
    }

    public void updatePassword(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRepo.updatePassword(passwordEncoder.encode(password), email);
    }
    public boolean verifyOTP(String email, int otp) {
        if (!otpMap.containsKey(email)) return false;
        OTPDetails details = otpMap.get(email);
        if (System.currentTimeMillis() > details.getExpiryTime()) {
            otpMap.remove(email);
            return false;
        }
        boolean valid = details.getCode() == otp;
        if (valid) otpMap.remove(email);
        return valid;
    }

    public long getRemainingTime(String email) {
        if (!otpMap.containsKey(email)) return 0;
        long remaining = otpMap.get(email).getExpiryTime() - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public void saveOTP(String email, int code, int minutesValid) {
        long expiryTime = System.currentTimeMillis() + minutesValid * 60 * 1000; // 10 minutes
        otpMap.put(email, new OTPDetails(code, expiryTime));

    }
}
