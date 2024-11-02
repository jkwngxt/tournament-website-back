package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    Team findByCaptainUserId(Integer user_id);
    List<Team> findByJoinAsParticipantRelationshipsTournamentTournamentId(UUID id);
    Team findByJoinAsParticipantRelationshipsUserUserIdAndJoinAsParticipantRelationshipsTournamentTournamentId(Integer userId, UUID id);
    boolean existsByTeamNameAndJoinAsParticipantRelationshipsTournamentTournamentId(String name, UUID tournamentId);
}
