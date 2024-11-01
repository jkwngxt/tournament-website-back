package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.AdminRepository;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import ku.th.tournamentwebsiteback.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private AdminRepository adminRepository;

    // Get all tournaments
    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Get a tournament by ID
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable UUID id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    // Create a tournament
    @PostMapping("/add")
    public ResponseEntity<?> createTournament(@RequestBody TournamentRequest request) {
        tournamentService.createTournament(request);
        return ResponseEntity.ok("Tournament created successfully");
    }

    // Update a tournament
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable UUID id, @RequestBody UpdateTournamentRequest request) {
        tournamentService.updateTournament(id, request);
        return ResponseEntity.ok("Tournament updated successfully");
    }
}


