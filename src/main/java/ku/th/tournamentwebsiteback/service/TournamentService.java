package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import ku.th.tournamentwebsiteback.response.TournamentProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service

public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TournamentProfileResponse> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }

    public TournamentProfileResponse getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));
        return convertToDto(tournament);
    }


    public TournamentProfileResponse createTournament(TournamentRequest request) {
        Tournament tournament = modelMapper.map(request, Tournament.class);
        return convertToDto(tournamentRepository.save(tournament));
    }

    public void updateTournament(UUID tournamentId, UpdateTournamentRequest request) {
        Tournament existingTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(request, existingTournament);
        tournamentRepository.save(existingTournament);
    }

    public TournamentProfileResponse getLatestTournament() {
        Tournament latestTournament = tournamentRepository.findTopByOrderByStartQualifierDateTimeDesc();
        if (latestTournament == null) {
            throw new EntityNotFoundException("No latest tournament found");
        }
        return convertToDto(latestTournament);
    }

    public TournamentProfileResponse getCurrentTournaments() {
        ZonedDateTime now = ZonedDateTime.now();
        Tournament currentTournament = tournamentRepository.findByStartQualifierDateTimeBeforeAndEndDateTimeAfter(now, now);

        if (currentTournament == null) {
            throw new EntityNotFoundException("No current tournament found");
        }

        return convertToDto(currentTournament);
    }


    private TournamentProfileResponse convertToDto(Tournament tournament) {
        return modelMapper.map(tournament, TournamentProfileResponse.class);
    }
}
