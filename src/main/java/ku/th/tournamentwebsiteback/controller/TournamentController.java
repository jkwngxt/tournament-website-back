package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.dto.TournamentDTO;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import ku.th.tournamentwebsiteback.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    // Get all tournaments
    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        List<TournamentDTO> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Get a tournament by ID
    @GetMapping("/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable UUID id) {
        TournamentDTO tournament = tournamentService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    // Create a tournament
    @PostMapping("/add")
    public ResponseEntity<?> createTournament(@RequestBody TournamentRequest request) {
        tournamentService.createTournament(request);
        return ResponseEntity.ok("Tournament created successfully");
    }

    // Update the tournament
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable UUID id, @RequestBody UpdateTournamentRequest request) {
        tournamentService.updateTournament(id, request);
        return ResponseEntity.ok("Tournament updated successfully");
    }
}


