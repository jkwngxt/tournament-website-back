package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.dto.TeamDetailDTO;
import ku.th.tournamentwebsiteback.dto.UserProfileDTO;
import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.entity.Team;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.repository.*;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TournamentRepository tournamentRepository;
    @Autowired
    JoinAsStaffRepository staffRepository;
    @Autowired
    JoinAsParticipantRepository participantRepository;


    public List<TeamDetailDTO> getAllTeams() {
        return teamRepository.findAll().stream().map(this::toDetailDTO).collect(Collectors.toList());
    }

    public TeamDetailDTO getTeamById(UUID id) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            return null;
        }
        return toDetailDTO(team);
    }

    public void validateTeam(UUID teamId, ValidateTeamRequest request) {
        Team existingTournament = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        modelMapper.map(request, existingTournament);
        teamRepository.save(existingTournament);
    }

    public List<TeamDetailDTO> getTeamByTournamentId(UUID id) {
        List<Team> teams = teamRepository.findByJoinAsParticipantRelationshipsTournamentTournamentId(id);
        return teams.stream().map(this::toDetailDTO).collect(Collectors.toList());
    }

    public TeamDetailDTO getTeamByTournamentIdAndUserId(Integer userId, UUID id) {
        Team team = teamRepository
                .findByJoinAsParticipantRelationshipsUserUserIdAndJoinAsParticipantRelationshipsTournamentTournamentId(userId, id);

        if(team == null) {
            return null;
        }

        return toDetailDTO(team);
    }

    public void createTeam(TeamRequest request) throws BadRequestException {
        Tournament tournament = getTournament(request.getTournamentId());
        validateTeamRegistrationPeriod(tournament);
        validateTeamMemberCount(request, tournament);

        User captain = getUserById(request.getCaptainUserId());
        List<User> members = getValidTeamMembers(request, tournament);

        Team team = buildTeam(request, captain, members, tournament);
        teamRepository.save(team);
    }

    private Tournament getTournament(UUID tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
    }

    private void validateTeamRegistrationPeriod(Tournament tournament) throws BadRequestException {
        if (!tournament.isInTeamRegisPeriod()) {
            throw new BadRequestException("This is not within the team registration period.");
        }
    }

    private void validateTeamMemberCount(TeamRequest request, Tournament tournament) throws BadRequestException {
        if (request.getMemberIdList().size() != tournament.getTeamMemberAmount()) {
            throw new BadRequestException("Invalid team member count");
        }
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private List<User> getValidTeamMembers(TeamRequest request, Tournament tournament) throws BadRequestException {
        List<User> members = new ArrayList<>();
        for (Integer userId : request.getMemberIdList()) {
            User member = getUserById(userId);
            validateUserEligibility(member, tournament);
            members.add(member);
        }
        return members;
    }

    private Team buildTeam(TeamRequest request, User captain, List<User> members, Tournament tournament) {
        Team team = modelMapper.map(request, Team.class);
        team.setCaptain(captain);
        List<JoinAsParticipantRelationship> participants = members.stream()
                .map(member -> createParticipantRelationship(member, team, tournament))
                .collect(Collectors.toList());
        team.setJoinAsParticipantRelationships(participants);
        participantRepository.saveAll(participants);
        return team;
    }

    private JoinAsParticipantRelationship createParticipantRelationship(User member, Team team, Tournament tournament) {
        JoinAsParticipantRelationship participant = new JoinAsParticipantRelationship();
        participant.setTeam(team);
        participant.setUser(member);
        participant.setTournament(tournament);
        return participant;
    }

    private void validateUserEligibility(User member, Tournament tournament) throws BadRequestException {
        if (participantRepository.existsByUserAndTournament(member, tournament)) {
            throw new BadRequestException("Some users are already in another team.");
        }
        if (staffRepository.existsByUserAndTournament(member, tournament)) {
            throw new BadRequestException("Some users are already in the staff team.");
        }
    }

    private TeamDetailDTO toDetailDTO(Team team) {
        TeamDetailDTO dto = modelMapper.map(team, TeamDetailDTO.class);
        dto.setMembers(team.getJoinAsParticipantRelationships()
                .stream()
                .map(rel -> modelMapper.map(rel.getUser(), UserProfileDTO.class))
                .collect(Collectors.toList()));
        dto.setTournamentId(team.getJoinAsParticipantRelationships().get(0).getTournament().getTournamentId());
        return dto;
    }
}
