package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import ku.th.tournamentwebsiteback.exception.BadRequestException;
import ku.th.tournamentwebsiteback.repository.*;
import ku.th.tournamentwebsiteback.request.QualifierMatchRequest;
import ku.th.tournamentwebsiteback.response.QualifierMatchDetailResponse;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class QualifierMatchService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QualifierMatchRepository qualifierMatchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private JudgeRepository judgeRepository;
    @Autowired
    private JoinAsStaffRepository joinAsStaffRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private DTOConvertor convertor;


    public List<QualifierMatchDetailResponse> findQualifierMatchesByTournamentId(UUID tournamentId) {
        List<QualifierMatch> matches = qualifierMatchRepository.findByTournamentTournamentId(tournamentId);
        return matches.stream()
                .map(this::qualifierMatchToDto)
                .collect(toList());
    }

    public QualifierMatchDetailResponse createQualifierMatch(UUID tournamentId, QualifierMatchRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));

        QualifierMatch qualifierMatch = modelMapper.map(request, QualifierMatch.class);
        qualifierMatch.setTournament(tournament);

        return qualifierMatchToDto(qualifierMatchRepository.save(qualifierMatch));
    }

    public void deleteQualifierMatch(UUID lobbyId) {
        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found with id: " + lobbyId));

        // Remove qualifier match reference from teams
        List<Team> teams = qualifierMatch.getTeams();
        for (Team team : teams) {
            team.setQualifierMatch(null);
            teamRepository.save(team);
        }

        // Delete judges associated with the qualifier match
        List<Judge> judges = qualifierMatch.getJudges();
        for (Judge judge : judges) {
            judgeRepository.delete(judge);
        }

        qualifierMatchRepository.delete(qualifierMatch);
    }

    public void joinQualifierMatchAsTeam(UUID lobbyId, Integer userId) {

        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        Team team = teamRepository.findByCaptainUserId(userId);

        if (team == null) {
            throw new BadRequestException("User is not a team captain in this tournament");
        }

        if (team.getQualifierMatch() != null) {
            throw new BadRequestException("Team is already registered in a qualifier match");
        }

        team.setQualifierMatch(qualifierMatch);
        teamRepository.save(team);
    }

    public void leaveQualifierMatchAsTeam(UUID lobbyId, Integer userId) {

        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        Team team = teamRepository.findByCaptainUserId(userId);

        if (team == null) {
            throw new BadRequestException("User is not a team captain in this tournament");
        }

        if (!qualifierMatch.getTeams().contains(team)) {
            throw new BadRequestException("Team is not part of this qualifier match");
        }

        team.setQualifierMatch(null);
        teamRepository.save(team);
    }

    public void joinQualifierMatchAsStaff(UUID lobbyId, Integer userId) {

        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        JoinAsStaffRelationship staffRelationship = joinAsStaffRepository.findByUserAndTournament(user, qualifierMatch.getTournament());

        if (staffRelationship == null) {
            throw new BadRequestException("User is not registered as staff for this tournament");
        }

        if (judgeRepository.findByJoinAsStaffRelationshipAndQualifierMatchLobbyId(staffRelationship, lobbyId) != null) {
            throw new BadRequestException("Staff is already registered in this qualifier match");
        }

        Judge judge = new Judge();
        judge.setId(new JudgePK(new JoinAsStaffRelationshipPK(userId, staffRelationship.getId().getTournamentId()), lobbyId));
        judge.setJoinAsStaffRelationship(staffRelationship);
        judge.setQualifierMatch(qualifierMatch);
        judgeRepository.save(judge);
    }

    public void leaveQualifierMatchAsStaff(UUID lobbyId, Integer userId) {
        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found with id: " + lobbyId));

        Judge judge = judgeRepository.findByJoinAsStaffRelationshipUserUserIdAndQualifierMatchLobbyId(userId, lobbyId);
        if (judge == null) {
            throw new BadRequestException("Staff member is not part of this qualifier match");
        }

        judgeRepository.delete(judge);
    }

    public QualifierMatchDetailResponse qualifierMatchToDto(QualifierMatch qualifierMatch) {
        return convertor.qualifierMatchToDto(qualifierMatch);
    }

    public QualifierMatchDetailResponse findQualifierMatchesById(UUID id) {
        return qualifierMatchToDto(qualifierMatchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found")));
    }
}
