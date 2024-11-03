package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.Users;
import ku.th.tournamentwebsiteback.repository.JoinAsParticipantRepository;
import ku.th.tournamentwebsiteback.repository.JoinAsStaffRepository;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public UserProfileResponse convertToUserProfileResponse(Integer userId, UUID tournamentId) {
        Users user = getUser(userId);
        Tournament tournament = null;
        if (tournamentId != null) {
            tournament = getTournament(tournamentId);
        }

        UserProfileResponse userProfileResponse = modelMapper.map(user, UserProfileResponse.class);
        System.out.println(userProfileResponse);
        userProfileResponse.setRefTournamentId(tournamentId);
        userProfileResponse.setRole(getRoleInTournament(user, tournament));

        return userProfileResponse;
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

    public Users getUser(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Tournament getTournament(UUID tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null);
    }
}
