package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.Team;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TeamRepository;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ModelMapper modelMapper;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(UUID id) {
        return teamRepository.findById(id).orElse(null);
    }

    public void createTeam(TeamRequest request) {
        Team team = modelMapper.map(request, Team.class);
        teamRepository.save(team);
    }

    public void validateTeam(UUID teamId, ValidateTeamRequest request) {
        Team existingTournament = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        modelMapper.map(request, existingTournament);
        teamRepository.save(existingTournament);
    }
}
