package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {

}
