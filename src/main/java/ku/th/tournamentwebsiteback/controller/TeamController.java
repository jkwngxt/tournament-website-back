package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import ku.th.tournamentwebsiteback.response.TeamDetailResponse;
import ku.th.tournamentwebsiteback.response.TeamProfileResponse;
import ku.th.tournamentwebsiteback.service.SecurityService;
import ku.th.tournamentwebsiteback.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private SecurityService securityService;

    @GetMapping
    public ResponseEntity<List<TeamDetailResponse>> getAllTeams() {
        List<TeamDetailResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailResponse> getTeamById(@PathVariable UUID id) {
        TeamDetailResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    @PostMapping
    public ResponseEntity<TeamDetailResponse> createTeam(@RequestBody TeamRequest request) {
        TeamDetailResponse team = teamService.createTeam(request);
        return ResponseEntity.status(201).body(team);
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<String> validateTeam(@PathVariable UUID id, @RequestBody ValidateTeamRequest request) {
        teamService.validateTeam(id, request);
        return ResponseEntity.ok("Team validated successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamProfileResponse>> getAllTeamsByUserId(@PathVariable Integer userId) {
        List<TeamProfileResponse> teams = teamService.getAllTeamDetailByUserId(userId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<TeamDetailResponse>> getTeamsByTournamentId(@PathVariable UUID tournamentId) {
        List<TeamDetailResponse> teams = teamService.getTeamByTournamentId(tournamentId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/tournament/{tournamentId}/me")
    public ResponseEntity<TeamDetailResponse> getTeamByTournamentIdAndUserId(@PathVariable UUID tournamentId) {
        Integer userId = securityService.getCurrentUserId();
        TeamDetailResponse team = teamService.getTeamByTournamentIdAndUserId(userId, tournamentId);
        return ResponseEntity.ok(team);
    }
}
