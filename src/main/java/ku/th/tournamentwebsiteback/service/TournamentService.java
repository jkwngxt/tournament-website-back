package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import ku.th.tournamentwebsiteback.request.TournamentRequest;
import ku.th.tournamentwebsiteback.request.UpdateTournamentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(UUID id) {
        return tournamentRepository.findById(id).orElse(null);
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
