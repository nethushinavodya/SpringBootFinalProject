package org.example.gamearenax_backend.controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.example.gamearenax_backend.dto.AuthDTO;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.service.UserService;
import org.example.gamearenax_backend.service.impl.UserServiceImpl;
import org.example.gamearenax_backend.util.JwtUtil;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            int res = userService.saveUser(userDTO);

            switch (res){
                case VarList.Created -> {
                    request.getSession().setAttribute("email", userDTO.getEmail());
                    request.getSession().setAttribute("role", userDTO.getRole());
                    System.out.println(request.getSession().getAttribute("email"));
                    System.out.println(request.getSession().getAttribute("role")+ "kkkkkkkk");
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    authDTO.setUsername(userDTO.getUsername());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, " Success",authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used",null));
                }
                case VarList.Conflict -> {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ResponseDTO(VarList.Conflict, "Username Already Used",null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Request,"Error",null));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllUsers(){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", userService.getAllUsers()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
     
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody UserDTO userDTO){
        try {
            System.out.println(userDTO.getCountry() + " " + userDTO.getUsername());
            int res = userService.updateUser(userDTO);

            if(res == VarList.Created){
                Object user = userService.getUserByUsername(userDTO.getUsername());
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", user));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseDTO(VarList.Bad_Request, "Error", null));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    /*get user by role admin and user*/
    @GetMapping("get-all-admins-and-users")
    public ResponseEntity<ResponseDTO> getAllAdminsAndUsers() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", userService.getAllAdminsAndUsers()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    // Ban user (set status to deactivated)
    @PutMapping("/ban")
    public ResponseEntity<ResponseDTO> banUser(@RequestParam String email) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "User banned successfully", userService.deleteUser(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    // Unban user (set status to active)
    @PutMapping("/unban")
    public ResponseEntity<ResponseDTO> unbanUser(@RequestParam String email) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "User unbanned successfully", userService.activateUser(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseDTO> getUserByEmail(@RequestParam String email) {
        try {
            UserDTO user = userService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", user));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("get-by-username")
    public ResponseEntity<ResponseDTO> getUserByUsername(@RequestParam String username) {
        try {
            UserDTO user = userService.getUserByUsername(username);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", user));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
