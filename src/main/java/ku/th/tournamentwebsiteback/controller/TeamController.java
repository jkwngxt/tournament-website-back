package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.response.TeamDetailResponse;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import ku.th.tournamentwebsiteback.service.SecurityService;
import ku.th.tournamentwebsiteback.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    SecurityService securityService;

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDetailResponse>> getAllTeams() {
        List<TeamDetailResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDetailResponse> getTeamById(@PathVariable UUID id) {
        TeamDetailResponse team = teamService.getTeamById(id);
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
    public ResponseEntity<List<TeamDetailResponse>> getTeamByTournamentId(@PathVariable UUID id) {
        List<TeamDetailResponse> teams = teamService.getTeamByTournamentId(id);
        return ResponseEntity.ok(teams);
    }

    // Get team's user in specific tournament
    @GetMapping("/tournaments/{tournamentId}/teams/me")
    public ResponseEntity<TeamDetailResponse> getTeamByTournamentIdAndUserId(@PathVariable UUID tournamentId) {
        Integer userId = securityService.getCurrentUserId();
        TeamDetailResponse team = teamService.getTeamByTournamentIdAndUserId(userId, tournamentId);
        return ResponseEntity.ok(team);
    }
}
