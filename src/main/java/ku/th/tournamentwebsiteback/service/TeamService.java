package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.entity.Team;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import ku.th.tournamentwebsiteback.exception.BadRequestException;
import ku.th.tournamentwebsiteback.repository.*;
import ku.th.tournamentwebsiteback.request.TeamRequest;
import ku.th.tournamentwebsiteback.request.ValidateTeamRequest;
import ku.th.tournamentwebsiteback.response.TeamDetailResponse;
import ku.th.tournamentwebsiteback.response.TeamProfileResponse;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
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
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private JoinAsStaffRepository staffRepository;
    @Autowired
    private JoinAsParticipantRepository participantRepository;


    public List<TeamDetailResponse> getAllTeams() {
        return teamRepository.findAll().stream().map(this::toDetailDTO).collect(Collectors.toList());
    }

    public TeamDetailResponse getTeamById(UUID id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
        return toDetailDTO(team);
    }

    public void validateTeam(UUID teamId, ValidateTeamRequest request) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));
        modelMapper.map(request, existingTeam);
        teamRepository.save(existingTeam);
    }

    public List<TeamDetailResponse> getTeamByTournamentId(UUID id) {
        List<Team> teams = teamRepository.findByJoinAsParticipantRelationshipsTournamentTournamentId(id);
        return teams.stream().map(this::toDetailDTO).collect(Collectors.toList());
    }

    public TeamDetailResponse getTeamByTournamentIdAndUserId(Integer userId, UUID id) {
        Team team = teamRepository
                .findByJoinAsParticipantRelationshipsUserUserIdAndJoinAsParticipantRelationshipsTournamentTournamentId(userId, id);

        if (team == null) {
            throw new EntityNotFoundException("Team not found for user " + userId + " in tournament " + id);
        }

        return toDetailDTO(team);
    }

    public TeamDetailResponse createTeam(TeamRequest request) {
        Tournament tournament = getTournament(request.getTournamentId());
        validateTeamRegistrationPeriod(tournament);

        if (teamRepository.existsByTeamNameAndJoinAsParticipantRelationshipsTournamentTournamentId(request.getTeamName(), request.getTournamentId())) {
            throw new BadRequestException("A team with the name '" + request.getTeamName() + "' already exists in this tournament.");
        }

        int totalMembers = request.getMemberIdList().size() + 1;
        validateTeamMemberCount(totalMembers, tournament);

        User captain = getUserById(request.getCaptainUserId());
        List<User> members = getValidTeamMembers(request, tournament, captain);

        Team team = buildTeam(request, captain, members, tournament);
        return toDetailDTO(team);
    }

    private Tournament getTournament(UUID tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
    }

    private void validateTeamRegistrationPeriod(Tournament tournament) {
        if (!tournament.isInTeamRegisPeriod()) {
            throw new BadRequestException("This is not within the team registration period.");
        }
    }

    private void validateTeamMemberCount(int totalMembers, Tournament tournament) {
        if (totalMembers != tournament.getTeamMemberAmount()) {
            throw new BadRequestException("Invalid team member count. Expected: "
                    + tournament.getTeamMemberAmount() + ", but got: " + totalMembers);
        }
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private List<User> getValidTeamMembers(TeamRequest request, Tournament tournament, User captain) {
        List<User> members = new ArrayList<>();

        validateUserEligibility(captain, tournament);
        members.add(captain);

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

        team = teamRepository.save(team);

        Team finalTeam = team;
        List<JoinAsParticipantRelationship> participants = members.stream()
                .map(member -> createParticipantRelationship(member, finalTeam, tournament))
                .collect(Collectors.toList());

        team.setJoinAsParticipantRelationships(participants);

        participantRepository.saveAll(participants);

        return team;
    }



    private JoinAsParticipantRelationship createParticipantRelationship(User member, Team team, Tournament tournament) {
        JoinAsParticipantRelationship participant = new JoinAsParticipantRelationship();

        JoinAsParticipantsRelationshipPK pk = new JoinAsParticipantsRelationshipPK(
                member.getUserId(),
                team.getTeamId(),
                tournament.getTournamentId()
        );
        participant.setId(pk);

        participant.setTeam(team);
        participant.setUser(member);
        participant.setTournament(tournament);

        return participant;
    }



    private void validateUserEligibility(User member, Tournament tournament) {
        if (participantRepository.existsByUserAndTournament(member, tournament)) {
            throw new BadRequestException("User " + member.getUserId() + " is already in another team.");
        }
        if (staffRepository.existsByUserAndTournament(member, tournament)) {
            throw new BadRequestException("User " + member.getUserId() + " is already in the staff team.");
        }
    }

    private TeamDetailResponse toDetailDTO(Team team) {
        TeamDetailResponse dto = modelMapper.map(team, TeamDetailResponse.class);
        dto.setMembers(team.getJoinAsParticipantRelationships()
                .stream()
                .map(rel -> modelMapper.map(rel.getUser(), UserProfileResponse.class))
                .collect(Collectors.toList()));
        dto.setTournamentId(team.getJoinAsParticipantRelationships().get(0).getTournament().getTournamentId());
        return dto;
    }

    public List<TeamProfileResponse> getAllTeamDetailByUserId(Integer userId) {
        List<JoinAsParticipantRelationship> relationships = participantRepository.findByUserUserId(userId);

        List<Team> teams = relationships.stream()
                .map(JoinAsParticipantRelationship::getTeam)
                .toList();

        return teams.stream()
                .map(team -> modelMapper.map(team, TeamProfileResponse.class))
                .collect(Collectors.toList());
    }
}
