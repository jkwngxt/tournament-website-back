package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.dto.TournamentProfileDTO;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TournamentProfileDTO> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(this::tournamentToDto)
                .collect(toList());
    }

    public TournamentProfileDTO getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament == null) {
            return null;
        }

        return tournamentToDto(tournament);
    }

    public void createTournament(TournamentRequest request) {
        Tournament tournament = modelMapper.map(request, Tournament.class);
        tournamentRepository.save(tournament);
    }

    public void updateTournament(UUID tournamentId, UpdateTournamentRequest request) {
        Tournament existingTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        modelMapper.map(request, existingTournament);
        tournamentRepository.save(existingTournament);
    }

    public TournamentProfileDTO getLatestTournament() {
        Optional<Tournament> latestTournament = tournamentRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "startDate"))).stream().findFirst();
        return latestTournament.map(this::tournamentToDto).orElse(null);
    }

    public List<TournamentProfileDTO> getCurrentTournament() {
        ZonedDateTime now = ZonedDateTime.now();

        return tournamentRepository.findAll()
                .stream()
                .filter(tournament -> tournament.getStartQualifierDateTime().isBefore(now) &&
                        tournament.getEndDateTime().isAfter(now))
                .map(this::tournamentToDto)
                .collect(Collectors.toList());
    }

    public TournamentProfileDTO tournamentToDto(Tournament tournament) {
        return modelMapper.map(tournament, TournamentProfileDTO.class);
    }
}
