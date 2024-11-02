package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.response.QualifierMatchDetailResponse;
import ku.th.tournamentwebsiteback.request.QualifierMatchRequest;
import ku.th.tournamentwebsiteback.service.QualifierMatchService;
import ku.th.tournamentwebsiteback.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class QualifierLobbyController {

    @Autowired
    QualifierMatchService qualifierMatchService;
    @Autowired
    SecurityService securityService;

    @PostMapping("/tournaments/{tournamentId}/qualifier-matches")
    public ResponseEntity<String> createQualifierMatch(@PathVariable UUID tournamentId, @RequestBody QualifierMatchRequest request) {
        qualifierMatchService.createQualifierMatch(tournamentId, request);
        return ResponseEntity.ok("Qualifier match has been successfully created.");
    }

    @GetMapping("/tournaments/{tournamentId}/qualifier-matches")
    public ResponseEntity<List<QualifierMatchDetailResponse>> getQualifierMatches(@PathVariable UUID tournamentId) {
        List<QualifierMatchDetailResponse> matches = qualifierMatchService.findQualifierMatchesByTournamentId(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @DeleteMapping("/qualifier-matches/{lobbyId}")
    public ResponseEntity<String> deleteQualifierMatch(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.deleteQualifierMatch(lobbyId, userId);
        return ResponseEntity.ok("Qualifier match has been successfully deleted.");
    }

    @PostMapping("/qualifier-matches/{lobbyId}/teams/join")
    public ResponseEntity<String> joinQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.joinQualifierMatchAsTeam(lobbyId, userId);
        return ResponseEntity.ok("Successfully joined the qualifier match as a team.");
    }


    @PostMapping("/qualifier-matches/{lobbyId}/teams/leave")
    public ResponseEntity<String> leaveQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.leaveQualifierMatchAsTeam(lobbyId, userId);
        return ResponseEntity.ok("Successfully left the qualifier match as a team.");
    }

    @PostMapping("/qualifier-matches/{lobbyId}/staffs/join")
    public ResponseEntity<String> joinQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.joinQualifierMatchAsStaff(lobbyId, userId);
        return ResponseEntity.ok("Successfully joined the qualifier match as a staff member.");
    }

    @PostMapping("/qualifier-matches/{lobbyId}/staffs/leave")
    public ResponseEntity<String> leaveQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.leaveQualifierMatchAsStaff(lobbyId, userId);
        return ResponseEntity.ok("Successfully left the qualifier match as a staff member.");
    }
}
