package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.dto.TeamDetailDTO;
import ku.th.tournamentwebsiteback.exception.UnauthorizedException;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import ku.th.tournamentwebsiteback.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDetailDTO>> getAllTeams() {
        List<TeamDetailDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDetailDTO> getTeamById(@PathVariable UUID id) {
        TeamDetailDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    // Create team
    @PostMapping("/teams")
    public ResponseEntity<String> createTeam(@RequestBody TeamRequest request) {
        teamService.createTeam(request);
        return ResponseEntity.ok("Team created successfully");
    }

    // Validate team = {rejected/approved}
    @PutMapping("/teams/{id}/validate")
    public ResponseEntity<String> validateTeam(@PathVariable UUID id, @RequestBody ValidateTeamRequest request) {
        teamService.validateTeam(id, request);
        return ResponseEntity.ok("Team validated successfully");
    }

    // Get all teams in Tournament
    @GetMapping("/tournaments/{tournamentId}/teams")
    public ResponseEntity<List<TeamDetailDTO>> getTeamByTournamentId(@PathVariable UUID id) {
        List<TeamDetailDTO> teams = teamService.getTeamByTournamentId(id);
        return ResponseEntity.ok(teams);
    }

    // Get team's user in specific tournament
    @GetMapping("/tournaments/{tournamentId}/teams/me")
    public ResponseEntity<TeamDetailDTO> getTeamByTournamentIdAndUserId(@PathVariable UUID tournamentId) {
        Integer userId = getCurrentUserId();
        TeamDetailDTO team = teamService.getTeamByTournamentIdAndUserId(userId, tournamentId);
        return ResponseEntity.ok(team);
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
