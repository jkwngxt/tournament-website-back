package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.*;
import ku.th.tournamentwebsiteback.repository.JoinAsParticipantRepository;
import ku.th.tournamentwebsiteback.repository.JoinAsStaffRepository;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class DTOConvertor {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JoinAsStaffRepository staffRepository;
    @Autowired
    JoinAsParticipantRepository participantRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TournamentRepository tournamentRepository;

    public UserProfileResponse idConvertToUserProfileResponse(Integer userId, UUID tournamentId) {
        Users user = getUser(userId);
        Tournament tournament = null;
        if (tournamentId != null) {
            tournament = getTournament(tournamentId);
        }

        return obgConvertToUserProfileResponse(user, tournament);
    }

    public UserProfileResponse obgConvertToUserProfileResponse(Users user, Tournament tournament) {

        UserProfileResponse userProfileResponse = modelMapper.map(user, UserProfileResponse.class);
        System.out.println(userProfileResponse);
        userProfileResponse.setRefTournamentId(tournament == null? null:tournament.getTournamentId());
        userProfileResponse.setRole(getRoleInTournament(user, tournament));

        return userProfileResponse;
    }

    public QualifierMatchDetailResponse qualifierMatchToDto(QualifierMatch qualifierMatch) {
        QualifierMatchDetailResponse response = modelMapper.map(qualifierMatch, QualifierMatchDetailResponse.class);
        Tournament tournament = qualifierMatch.getTournament();

        // Map judges
        List<UserProfileResponse> userProfileResponses = qualifierMatch.getJudges().stream()
                .map(judge -> {
                    Users judgeUser = judge.getJoinAsStaffRelationship().getUser();
                    return obgConvertToUserProfileResponse(judgeUser, tournament);
                })
                .collect(toList());
        response.setJudges(userProfileResponses);

        return response;
    }


    public TeamDetailResponse teamToDTO(Team team) {
        TeamDetailResponse dto = modelMapper.map(team, TeamDetailResponse.class);
        Tournament tournament = team.getQualifierMatch().getTournament();

        dto.setMembers(team.getJoinAsParticipantRelationships()
                .stream()
                .map(rel -> obgConvertToUserProfileResponse(rel.getUser(), tournament))
                .collect(Collectors.toList()));
        dto.setTournamentId(team.getJoinAsParticipantRelationships().get(0).getTournament().getTournamentId());
        return dto;
    }

    public String getRoleInTournament(Users user, Tournament tournament) {
        if (user.getAdmin() != null) {
            return "Admin";
        }

        if (tournament != null) {
            if (staffRepository.existsByUserAndTournament(user, tournament)) {
                return "Staff";
            }

            if (participantRepository.existsByUserAndTournament(user, tournament)) {
                return "Participant";
            }
        }

        return "Spectator";
    }

    public StaffRelationshipResponse convertToStaffResponse(JoinAsStaffRelationship staff) {
        StaffRelationshipResponse response = new StaffRelationshipResponse();
        Tournament tournament = staff.getTournament();

        response.setUser(obgConvertToUserProfileResponse(staff.getUser(), tournament));

        response.setPosition(staff.getPosition());
        response.setTournamentId(tournament.getTournamentId());
        response.setStatus(staff.getStatus());

        response.setQualifierMatchJudges(
                staff.getJudges().stream()
                        .map(judge -> modelMapper.map(judge.getQualifierMatch(), QualifierMatchProfileResponse.class))
                        .collect(Collectors.toList())
        );
        return response;
    }

    public Users getUser(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Tournament getTournament(UUID tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null);
    }
}
