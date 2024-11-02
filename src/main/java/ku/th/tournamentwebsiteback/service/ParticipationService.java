package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.repository.JoinAsParticipantRepository;
import ku.th.tournamentwebsiteback.response.ParticipationResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipationService {
    @Autowired
    JoinAsParticipantRepository participantRepository;
    @Autowired
    ModelMapper modelMapper;

    public List<ParticipationResponse> getAllParticipation() {
        return convertToDTO(participantRepository.findAll());
    }

    public List<ParticipationResponse> getAllParticipationByTournamentId(UUID tournamentId) {
        return convertToDTO(participantRepository.findByTournamentTournamentId(tournamentId));
    }

    public List<ParticipationResponse> getAllParticipationByUserId(Integer userId) {
        return convertToDTO(participantRepository.findByUserUserId(userId));
    }

    public List<ParticipationResponse> getAllParticipationByTeamId(UUID teamId) {
        return convertToDTO(participantRepository.findByTeamTeamId(teamId));
    }

    private List<ParticipationResponse> convertToDTO(List<JoinAsParticipantRelationship> participationList) {
        return participationList
                .stream()
                .map(participation -> {
                    ParticipationResponse participationResponse = new ParticipationResponse();
                    participationResponse.setTeamId(participation.getId().getTeamId());
                    participationResponse.setUserId(participation.getUser().getUserId());
                    participationResponse.setTournamentId(participation.getId().getTournamentId());
                    return participationResponse;
                })
                .toList();
    }
}
