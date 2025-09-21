
// Updated Controller
package org.example.gamearenax_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.gamearenax_backend.dto.BracketsDTO;
import org.example.gamearenax_backend.service.impl.BracketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/brackets")
@RequiredArgsConstructor
public class BracketController {

    private final BracketService bracketService;

    @PostMapping("/generate/{tournamentId}")
    public ResponseEntity<List<BracketsDTO>> generateBracket(@PathVariable Long tournamentId) {
        List<BracketsDTO> brackets = bracketService.generateBracket(tournamentId);
        return ResponseEntity.ok(brackets);
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<BracketsDTO>> getTournamentBracket(@PathVariable Long tournamentId) {
        List<BracketsDTO> brackets = bracketService.getTournamentBracket(tournamentId);
        return ResponseEntity.ok(brackets);
    }

    @PutMapping("/{matchId}/result")
    public ResponseEntity<BracketsDTO> updateMatchResult(
            @PathVariable Long matchId,
            @RequestParam Long winnerId) {
        BracketsDTO updatedMatch = bracketService.updateMatchResult(matchId, winnerId);
        return ResponseEntity.ok(updatedMatch);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<BracketsDTO> getMatch(@PathVariable Long matchId) {
        // Implementation for getting single match
        return ResponseEntity.ok().build();
    }
}