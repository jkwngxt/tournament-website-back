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
        return convertToDTO(participantRepository.findByTournamentId(tournamentId));
    }

    public List<ParticipationResponse> getAllParticipationByUserId(Integer userId) {
        return convertToDTO(participantRepository.findByUserId(userId));
    }

    public List<ParticipationResponse> getAllParticipationByTeamId(UUID teamId) {
        return convertToDTO(participantRepository.findByTeamId(teamId));
    }

    private List<ParticipationResponse> convertToDTO(List<JoinAsParticipantRelationship> participationList) {
        return participationList
                .stream()
                .map(participation->modelMapper.map(participation, ParticipationResponse.class))
                .toList();
    }
}
