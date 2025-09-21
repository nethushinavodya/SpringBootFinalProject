package org.example.gamearenax_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.dto.StreamerDTO;
import org.example.gamearenax_backend.dto.UserDTO;
import org.example.gamearenax_backend.entity.User;
import org.example.gamearenax_backend.service.StreamerService;
import org.example.gamearenax_backend.service.UserService;
import org.example.gamearenax_backend.util.JwtUtil;
import org.example.gamearenax_backend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/streamers")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class StreamerController {

    private final JwtUtil jwtUtil;
    @Autowired
    private  StreamerService streamerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public StreamerController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/addStreamer")
    public ResponseEntity<ResponseDTO> addStreamer(@RequestBody StreamerDTO streamerDTO, HttpServletRequest request){

        String email = (String) request.getAttribute("email");
        String role = (String) request.getAttribute("role");

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Email not found in token", null));
        }

        User user = userService.SearchByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
        }

        System.out.println("email: " + email);
        System.out.println("role: " + role);
        System.out.println(user.getUsername());

        try {
            int res = streamerService.addStreamer(streamerDTO,user);
            switch (res){
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(modelMapper.map(user, UserDTO.class));
                    request.setAttribute("role", "Streamer");
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", token));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseDTO(VarList.Not_Acceptable, "Streamer Already Exists", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseDTO(VarList.Bad_Request, "Error", null));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateStreamer(@RequestBody StreamerDTO streamerDTO){
        try {
            int res = streamerService.updateStreamer(streamerDTO);
            switch (res){
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseDTO(VarList.Bad_Request, "Error", null));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllStreamers(){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.getAllStreamers()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get by email
   @GetMapping("getByEmail")
    public ResponseEntity<ResponseDTO> getStreamerByEmail(@RequestParam String email){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.getStreamerByEmail(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get by isLive = true
    @GetMapping("getByIsLive")
    public ResponseEntity<ResponseDTO> getStreamerByIsLive(){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.getStreamerByIsLive()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //update isLive and StreamUrl
    @PutMapping("/updateIsLive")
    public ResponseEntity<ResponseDTO> updateIsLive(@RequestParam String email, @RequestParam("stream_url") String streamUrl){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.updateIsLive(email, streamUrl)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //update isLive as false
    @PatchMapping("/updateIsLiveFalse")
    public ResponseEntity<ResponseDTO> updateIsLiveFalse(@RequestParam String email){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.updateIsLiveFalse(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    @PatchMapping("/deleteStreamer")
    public ResponseEntity<ResponseDTO> deleteStreamer(@RequestParam String email){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", streamerService.deleteStreamer(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
