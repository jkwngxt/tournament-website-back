package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    Tournament findTopByOrderByStartQualifierDateTimeDesc();

    List<Tournament> findByStartQualifierDateTimeBeforeAndEndDateTimeAfter(ZonedDateTime now, ZonedDateTime now1);
}
