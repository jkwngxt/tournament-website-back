package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.QualifierMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QualifierMatchRepository extends JpaRepository<QualifierMatch, UUID> {

    List<QualifierMatch> findByTournamentTournamentId(UUID tournamentId);
}
