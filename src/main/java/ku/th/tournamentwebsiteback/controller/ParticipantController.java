package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.response.ParticipationResponse;
import ku.th.tournamentwebsiteback.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/participation")
public class ParticipantController {

    @Autowired
    private ParticipationService participationService;


    // get all participation
    @GetMapping
    public ResponseEntity<List<ParticipationResponse>> getAllParticipation() {
        List<ParticipationResponse> participationList = participationService.getAllParticipation();
        return ResponseEntity.ok(participationList);
    }

    // get all participation by specific tournament id
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<ParticipationResponse>> getParticipationByTournamentId(@PathVariable UUID tournamentId) {
        List<ParticipationResponse> participationList = participationService.getAllParticipationByTournamentId(tournamentId);
        return ResponseEntity.ok(participationList);
    }

    // get all participation by specific user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ParticipationResponse>> getParticipationByUserId(@PathVariable Integer userId) {
        List<ParticipationResponse> participationList = participationService.getAllParticipationByUserId(userId);
        return ResponseEntity.ok(participationList);
    }

    // get all participation by specific team id
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<ParticipationResponse>> getParticipationByTeamId(@PathVariable UUID teamId) {
        List<ParticipationResponse> participationList = participationService.getAllParticipationByTeamId(teamId);
        return ResponseEntity.ok(participationList);
    }
}
