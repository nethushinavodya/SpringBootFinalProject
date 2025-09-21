package org.example.gamearenax_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.ClanDTO;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.service.ClanService;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clan")
@RequiredArgsConstructor
public class ClanController {
    private final ClanService clanService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createClan(@RequestBody ClanDTO clanDTO) {
        try {
            ClanDTO createdClan = clanService.createClan(clanDTO);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clan created successfully", createdClan));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllClans() {
        try {

            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clans retrieved successfully", clanService.getAllClans()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

    // Get all clans that are open
    @GetMapping("/getAllClansByOpen")
    public ResponseEntity<ResponseDTO> getAllClansByOpen() {
        try {
            List<ClanDTO> clans = clanService.getAllClansByOpen();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clans retrieved successfully", clans));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

//    get clan by id
    @GetMapping("/getById")
    public ResponseEntity<ResponseDTO> getClanById(@RequestParam String id) {
        try {
            ClanDTO clan = clanService.getClanById(id);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clan retrieved successfully", clan));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

    //get all by ranking point in ascending order
    @GetMapping("/rankingPointsAsc")
    public ResponseEntity<ResponseDTO> getAllClansByRankingPointsAsc() {
        try {
            List<ClanDTO> clans = clanService.getAllClansByRankingPointsAsc();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clans retrieved successfully by ranking points ", clans));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }
    //get isjoinedclan
    @GetMapping("/isJoinedClan")
    public ResponseEntity<ResponseDTO> isJoinedClan(@RequestParam String username) {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Clans retrieved successfully", clanService.isJoinedClan(username)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

}
