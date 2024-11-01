package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TournamentService {
    @Autowired
    TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(UUID id) {
        return tournamentRepository.findById(id).orElse(null);
    }

//    public Map<String, Object> createTournament() {
//
//    }
}
