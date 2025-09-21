package org.example.gamearenax_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.JoinRequestDTO;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.repository.JoinRequestRepo;
import org.example.gamearenax_backend.service.ClanMemberService;
import org.example.gamearenax_backend.service.JoinRequestService;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("api/v1/join-request")
@RequiredArgsConstructor
public class JoinRequestController {

    private final JoinRequestService joinRequestService;
    private final ClanMemberService clanMemberService;


    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllJoinRequests() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", joinRequestService.getAllJoinRequests()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createJoinRequest(@RequestBody JoinRequestDTO joinRequestDTO) {
        try {
            int res = joinRequestService.createJoinRequest(joinRequestDTO);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", null));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseDTO(VarList.Not_Acceptable, "Join Request Already Exists", null));
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

    @PostMapping("/accept")
    public ResponseEntity<ResponseDTO> acceptJoinRequest(@RequestParam ("id") String id) {
        try {
            int res = joinRequestService.acceptJoinRequest(id);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", null));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseDTO(VarList.Not_Acceptable, "Join Request Already Accepted", null));
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

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteJoinRequest(@RequestParam ("id") String id) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", joinRequestService.deleteJoinRequest(id)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/getByClanId")
    public ResponseEntity<ResponseDTO> getJoinRequestByClanId(@RequestParam ("clanId") String clanId) {
        System.out.println(clanId);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", joinRequestService.getJoinRequestByClanId(clanId)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
