package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.response.QualifierMatchDetailResponse;
import ku.th.tournamentwebsiteback.request.QualifierMatchRequest;
import ku.th.tournamentwebsiteback.service.QualifierMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
public class QualifierLobbyController {

    @Autowired
    QualifierMatchService qualifierMatchService;

    @PostMapping("/add")
    public ResponseEntity<String> createLobby(@RequestBody QualifierMatchRequest request) {
        qualifierMatchService.createLobby(request);
        return ResponseEntity.ok("Lobby has been successfully created.");
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<List<QualifierMatchDetailResponse>> getQualifierMatches(@PathVariable UUID tournamentId) {
        List<QualifierMatchDetailResponse> matches = qualifierMatchService.findQualifierMatchesByTournamentId(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @DeleteMapping("/del/{lobbyId}")
    public ResponseEntity<String> deleteQualifierMatch(@PathVariable UUID lobbyId) {
        Integer userId = getCurrentUserId();
        qualifierMatchService.deleteQualifierMatch(lobbyId, userId);
        return ResponseEntity.ok("Lobby has been successfully deleted.");
    }

    @PostMapping("/{lobbyId}/join/team")
    public ResponseEntity<?> joinQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = 1;
        try {
            qualifierMatchService.joinQualifierMatchAsTeam(lobbyId, userId);
            return ResponseEntity.ok("Successfully joined the qualifier match as a team.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to join qualifier match as a team: " + ex.getMessage());
        }
    }

    @PostMapping("/{lobbyId}/leave/team")
    public ResponseEntity<?> leaveQualifierMatchAsTeam(@PathVariable UUID lobbyId) {
        Integer userId = getCurrentUserId();
        try {
            qualifierMatchService.leaveQualifierMatchAsTeam(lobbyId, userId);
            return ResponseEntity.ok("Successfully left the qualifier match as a team.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to leave qualifier match as a team: " + ex.getMessage());
        }
    }

    @PostMapping("/{lobbyId}/join/staff")
    public ResponseEntity<?> joinQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = getCurrentUserId();
        try {
            qualifierMatchService.joinQualifierMatchAsStaff(lobbyId, userId);
            return ResponseEntity.ok("Successfully joined the qualifier match as a staff member.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to join qualifier match as a staff member: " + ex.getMessage());
        }
    }

    @PostMapping("/{lobbyId}/leave/staff")
    public ResponseEntity<?> leaveQualifierMatchAsStaff(@PathVariable UUID lobbyId) {
        Integer userId = getCurrentUserId();
        try {
            qualifierMatchService.leaveQualifierMatchAsStaff(lobbyId, userId);
            return ResponseEntity.ok("Successfully left the qualifier match as a staff member.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to leave qualifier match as a staff member: " + ex.getMessage());
        }
    }

    private Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Current principal: " + principal);
        if (principal instanceof Integer) {
            return (Integer) principal;
        } else {
            throw new RuntimeException("Invalid user principal.");
        }
    }
}
