package org.example.gamearenax_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.dto.TournamentDTO;
import org.example.gamearenax_backend.entity.Game;
import org.example.gamearenax_backend.service.GameService;
import org.example.gamearenax_backend.service.TournamentService;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final GameService gameService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Streamer') || hasAuthority('Admin')")
    public ResponseEntity<ResponseDTO> addTournament(@RequestBody TournamentDTO tournamentDTO , HttpServletRequest request) {
        try {
            Game game = gameService.getGameByName(tournamentDTO.getGameName());
            if (game == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Request, "Invalid Game Name", null));
            }

            if (!game.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Request, "Game is not active", null));
            }
            String email = tournamentDTO.getStreamerEmail();
            tournamentDTO.setStreamerEmail(email);

            int res = tournamentService.addTournament(tournamentDTO,game);

            return switch (res) {
                case VarList.Created -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Tournament created successfully", null));
                case VarList.Not_Acceptable -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Not acceptable", null));
                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Request, "Bad request", null));
            };

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //update tournament
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('Streamer') || hasAuthority('Admin')")
    public ResponseEntity<ResponseDTO> updateTournament(@RequestBody TournamentDTO tournamentDTO) {
        try {
            int res = tournamentService.updateTournament(tournamentDTO);
            return switch (res) {
                case VarList.Created -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Tournament updated successfully", null));
                case VarList.Not_Acceptable -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Not acceptable", null));
                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Request, "Bad request", null));
            };
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get all tournaments
    @GetMapping("/getAllTournaments")
    public ResponseEntity<ResponseDTO> getAllTournaments() {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Success", tournamentService.getAllTournaments()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get by ongoing status ONGOING
    @GetMapping("/getByOngoingStatus")
    public ResponseEntity<ResponseDTO> getTournamentByOngoingStatus() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", tournamentService.getTournamentByOngoingStatus()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get UPCOMING tournaments
    @GetMapping("/getUpcomingTournaments")
    public ResponseEntity<ResponseDTO> getUpcomingTournaments() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", tournamentService.getUpcomingTournaments()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get SOLO tournaments
    @GetMapping("/getSoloTournaments")
    public ResponseEntity<ResponseDTO> getSoloTournaments() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", tournamentService.getSoloTournaments()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //get CLAN tournaments
    @GetMapping("/getClanTournaments")
    public ResponseEntity<ResponseDTO> getClanTournaments() {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", tournamentService.getClanTournaments()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    //update registration status
    @PatchMapping("/updateRegistrationStatus")
    @PreAuthorize("hasAuthority('Streamer') || hasAuthority('Admin')")
    public ResponseEntity<ResponseDTO> updateRegistrationStatus(@RequestParam String tournamentId, @RequestParam String registrationStatus) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(VarList.Created, "Success", tournamentService.updateRegistrationStatus(tournamentId, registrationStatus)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/getTournamentByStreamer")
    public ResponseEntity<ResponseDTO> getTournamentByStreamer(@RequestParam String email) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "Success", tournamentService.getTournamentByStreamer(email)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

//    get tournament by id
    @GetMapping("/getTournamentById")
    public ResponseEntity<ResponseDTO> getTournamentById(@RequestParam String id) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "Success", tournamentService.getTournamentById(id)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
