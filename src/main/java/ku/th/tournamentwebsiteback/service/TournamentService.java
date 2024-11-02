package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.dto.TournamentDTO;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TournamentDTO> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(this::tournamentToDto)
                .collect(toList());
    }

    public TournamentDTO getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament == null) {
            return null;
        }

        return tournamentToDto(tournament);
    }

    public TournamentDTO tournamentToDto(Tournament tournament) {
        return modelMapper.map(tournament, TournamentDTO.class);
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
}
