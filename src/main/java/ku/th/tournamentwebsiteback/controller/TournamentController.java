package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    // Get all tournaments
    @GetMapping
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


}

