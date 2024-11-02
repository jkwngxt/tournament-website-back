package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.dto.JudgeProfileDTO;
import ku.th.tournamentwebsiteback.dto.QualifierMatchDetailDTO;
import ku.th.tournamentwebsiteback.entity.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import ku.th.tournamentwebsiteback.repository.*;
import ku.th.tournamentwebsiteback.request.QualifierMatchRequest;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class QualifierMatchService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    QualifierMatchRepository qualifierMatchRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    JudgeRepository judgeRepository;
    @Autowired
    JoinAsStaffRepository joinAsStaffRepository;


    public List<QualifierMatchDetailDTO> findQualifierMatchesByTournamentId(UUID tournamentId) {

        List<QualifierMatch> matches = qualifierMatchRepository.findByTournamentTournamentId(tournamentId);
        return  matches.stream()
                .map(this::qualifierMatchToDto)
                .collect(toList());
    }

    public QualifierMatchDetailDTO qualifierMatchToDto(QualifierMatch qualifierMatch) {
        QualifierMatchDetailDTO qualifierMatchDetailDTO = modelMapper.map(qualifierMatch, QualifierMatchDetailDTO.class);
        List<Judge> judges = qualifierMatch.getJudges();
        for (Judge judge : judges) {
            qualifierMatchDetailDTO.getJudges().clear();
            JudgeProfileDTO judgeProfileDTO = new JudgeProfileDTO();
            judgeProfileDTO.setUserId(judge.getId().getJoinAsStaffRelationshipId().getUserId());
            judgeProfileDTO.setUsername(judge.getJoinAsStaffRelationship().getUser().getUsername());
            qualifierMatchDetailDTO.getJudges().add(judgeProfileDTO);
        }
        return qualifierMatchDetailDTO;
    }

    public void createLobby(QualifierMatchRequest request) {
        QualifierMatch qualifierMatch = modelMapper.map(request, QualifierMatch.class);
        qualifierMatchRepository.save(qualifierMatch);
    }

    public void deleteQualifierMatch(UUID lobbyId, Integer userId) {
        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!hasPermissionToDelete(user, qualifierMatch)) {
            throw new AccessDeniedException("User does not have permission to delete this qualifier match");
        }

        List<Team> teams = qualifierMatch.getTeams();
        for (Team team : teams) {
            team.setQualifierMatch(null);
            teamRepository.save(team);
        }

        List<Judge> judges = qualifierMatch.getJudges();
        for (Judge judge : judges) {
            judgeRepository.delete(judge);
        }

        qualifierMatchRepository.delete(qualifierMatch);
    }

    public void joinQualifierMatchAsTeam(UUID lobbyId, Integer userId) throws BadRequestException {

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

    public void leaveQualifierMatchAsTeam(UUID lobbyId, Integer userId) throws BadRequestException {

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

    public void joinQualifierMatchAsStaff(UUID lobbyId, Integer userId) throws BadRequestException {

        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        User user = userRepository.findById(userId)
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

    public void leaveQualifierMatchAsStaff(UUID lobbyId, Integer userId) throws BadRequestException {

        QualifierMatch qualifierMatch = qualifierMatchRepository.findById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Qualifier Match not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Judge judge = judgeRepository.findByJoinAsStaffRelationshipUserUserIdAndQualifierMatchLobbyId(userId, lobbyId);
        if (judge == null) {
            throw new BadRequestException("Staff member is not part of this qualifier match");
        }

        judgeRepository.delete(judge);
    }

    // ยังไม่ได้ทดสอบ
    private boolean hasPermissionToDelete(User user, QualifierMatch qualifierMatch) {
        return user.getAdmin() != null;
    }


}
