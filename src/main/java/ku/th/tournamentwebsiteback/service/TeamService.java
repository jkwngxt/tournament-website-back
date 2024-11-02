package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.dto.QualifierMatchProfileDTO;
import ku.th.tournamentwebsiteback.dto.TeamDetailDTO;
import ku.th.tournamentwebsiteback.dto.UserProfileDTO;
import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.entity.Team;
import ku.th.tournamentwebsiteback.repository.TeamRepository;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public TeamDetailDTO getTeamById(UUID id) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            return null;
        }
        return toDetailDTO(team);
    }

    public TeamDetailDTO toDetailDTO(Team team) {
        TeamDetailDTO teamDetailDTO = modelMapper.map(team, TeamDetailDTO.class);
        teamDetailDTO.setMembers(new ArrayList<>());
        List<JoinAsParticipantRelationship> participantRelationships = team.getJoinAsParticipantRelationships();
        for (JoinAsParticipantRelationship participantRelationship: participantRelationships) {
            teamDetailDTO.getMembers().add(modelMapper.map(participantRelationship.getUser(), UserProfileDTO.class));
        }
        teamDetailDTO.setTournamentId(participantRelationships.get(0).getTournament().getTournamentId());
        return teamDetailDTO;
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
