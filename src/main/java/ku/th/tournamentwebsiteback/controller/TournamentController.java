package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import ku.th.tournamentwebsiteback.response.TournamentProfileResponse;
import ku.th.tournamentwebsiteback.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<TournamentProfileResponse>> getTournaments() {
        List<TournamentProfileResponse> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Get latest tournaments
    @GetMapping("/latest")
    public ResponseEntity<TournamentProfileResponse> getLatestTournament() {
        TournamentProfileResponse tournament = tournamentService.getLatestTournament();
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    // Get current tournaments
    @GetMapping("/current")
    public ResponseEntity<List<TournamentProfileResponse>> getCurrentTournaments() {
        List<TournamentProfileResponse> tournaments = tournamentService.getCurrentTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Get a tournament by ID
    @GetMapping("/{id}")
    public ResponseEntity<TournamentProfileResponse> getTournamentById(@PathVariable UUID id) {
        TournamentProfileResponse tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }


    // Create a tournament
    @PostMapping
    public ResponseEntity<TournamentProfileResponse> createTournament(@RequestBody TournamentRequest request) {
        TournamentProfileResponse response = tournamentService.createTournament(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update the tournament
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable UUID id, @RequestBody UpdateTournamentRequest request) {
        tournamentService.updateTournament(id, request);
        return ResponseEntity.ok("Tournament updated successfully");
    }
}


