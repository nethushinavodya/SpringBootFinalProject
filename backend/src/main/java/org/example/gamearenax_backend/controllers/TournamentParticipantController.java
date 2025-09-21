package org.example.gamearenax_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.ResponseDTO;
import org.example.gamearenax_backend.dto.TournamentParticipantDTO;
import org.example.gamearenax_backend.service.TournamentParticipantService;
import org.example.gamearenax_backend.util.VarList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tournament-participant")
@RequiredArgsConstructor
public class TournamentParticipantController {
    private final TournamentParticipantService tournamentParticipantService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO> joinTournament(@RequestBody TournamentParticipantDTO tournamentParticipantDTO) {
        try {
            TournamentParticipantDTO joinedTournament = tournamentParticipantService.joinTournament(tournamentParticipantDTO);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Joined tournament successfully", joinedTournament));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }
    /*get participants by tournament id*/
    @GetMapping("/getParticipantsByTournamentId")
    public ResponseEntity<ResponseDTO> getParticipantsByTournamentId(@RequestParam String tournamentId) {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", tournamentParticipantService.getParticipantsByTournamentId(tournamentId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

    @GetMapping("/getTournamentsByPlayerEmail")
    public ResponseEntity<ResponseDTO> getTournamentsByPlayerEmail(@RequestParam String playerEmail) {
        if (playerEmail == null || playerEmail.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, "playerEmail is required", null));
        }
        try {
            // Call service to get tournaments
            Object tournaments = tournamentParticipantService.getTournamentsByPlayerEmail(playerEmail);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", tournaments));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Not_Acceptable, e.getMessage(), null));
        }
    }

}
