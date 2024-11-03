package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.request.QualifierMatchRequest;
import ku.th.tournamentwebsiteback.response.QualifierMatchDetailResponse;
import ku.th.tournamentwebsiteback.service.QualifierMatchService;
import ku.th.tournamentwebsiteback.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/qualifier-matches")
public class QualifierMatchController {
    @Autowired
    QualifierMatchService qualifierMatchService;
    @Autowired
    SecurityService securityService;

    @GetMapping("/{id}")
    public ResponseEntity<QualifierMatchDetailResponse> getQualifierMatchesByID(@PathVariable UUID id) {
        QualifierMatchDetailResponse matches = qualifierMatchService.findQualifierMatchesById(id);
        return ResponseEntity.ok(matches);
    }

    @PostMapping("/tournament/{tournamentId}")
    public ResponseEntity<QualifierMatchDetailResponse> createQualifierMatch(@PathVariable UUID tournamentId, @RequestBody QualifierMatchRequest request) {
        QualifierMatchDetailResponse response = qualifierMatchService.createQualifierMatch(tournamentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<QualifierMatchDetailResponse>> getQualifierMatches(@PathVariable UUID tournamentId) {
        List<QualifierMatchDetailResponse> matches = qualifierMatchService.findQualifierMatchesByTournamentId(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @DeleteMapping("/{lobbyId}") //must be admin
    public ResponseEntity<String> deleteQualifierMatch(@PathVariable UUID lobbyId) {
        qualifierMatchService.deleteQualifierMatch(lobbyId);
        return ResponseEntity.ok("Qualifier match has been successfully deleted.");
    }

    @PostMapping("/{lobbyId}/team/join")
    public ResponseEntity<String> joinQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.joinQualifierMatchAsTeam(lobbyId, userId);
        return ResponseEntity.ok("Successfully joined the qualifier match as a team.");
    }


    @PostMapping("/{lobbyId}/team/leave")
    public ResponseEntity<String> leaveQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.leaveQualifierMatchAsTeam(lobbyId, userId);
        return ResponseEntity.ok("Successfully left the qualifier match as a team.");
    }

    @PostMapping("/{lobbyId}/staff/join")
    public ResponseEntity<String> joinQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.joinQualifierMatchAsStaff(lobbyId, userId);
        return ResponseEntity.ok("Successfully joined the qualifier match as a staff member.");
    }

    @PostMapping("/{lobbyId}/staff/leave")
    public ResponseEntity<String> leaveQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = securityService.getCurrentUserId();
        qualifierMatchService.leaveQualifierMatchAsStaff(lobbyId, userId);
        return ResponseEntity.ok("Successfully left the qualifier match as a staff member.");
    }
}
