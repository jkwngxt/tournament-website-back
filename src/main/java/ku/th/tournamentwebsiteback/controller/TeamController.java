package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.dto.TeamDetailDTO;
import ku.th.tournamentwebsiteback.exception.UnauthorizedException;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import ku.th.tournamentwebsiteback.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamDetailDTO>> getAllTeams() {
        List<TeamDetailDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailDTO> getTeamById(@PathVariable UUID id) {
        TeamDetailDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    // Get all teams in Tournament
    @GetMapping("/tournament/{id}")
    public ResponseEntity<List<TeamDetailDTO>> getTeamByTournamentId(@PathVariable UUID id) {
        List<TeamDetailDTO> teams = teamService.getTeamByTournamentId(id);
        return ResponseEntity.ok(teams);
    }

    // Get team's user in specific tournament
    @GetMapping("/tournament/{id}/user")
    public ResponseEntity<TeamDetailDTO> getTeamByTournamentIdAndUserId(@PathVariable UUID id) {
        Integer userId = getCurrentUserId();
        TeamDetailDTO team = teamService.getTeamByTournamentIdAndUserId(userId, id);
        return ResponseEntity.ok(team);
    }

    // Validate team = {rejected/approved}
    @PutMapping("/validate/{id}")
    public ResponseEntity<String> validateTeam(@PathVariable UUID id, @RequestBody ValidateTeamRequest request) {
        teamService.validateTeam(id, request);
        return ResponseEntity.ok("Team validated successfully");
    }

    private Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Integer) {
            return (Integer) principal;
        } else {
            throw new UnauthorizedException("Invalid user principal.");
        }
    }
}
