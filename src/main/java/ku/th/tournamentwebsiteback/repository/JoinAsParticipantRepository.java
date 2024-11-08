package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.Users;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JoinAsParticipantRepository extends JpaRepository<JoinAsParticipantRelationship, JoinAsParticipantsRelationshipPK> {
    boolean existsByUserAndTournament(Users user, Tournament tournament);

    List<JoinAsParticipantRelationship> findByUserUserId(Integer userId);

    List<JoinAsParticipantRelationship> findByTournamentTournamentId(UUID tournamentId);

    List<JoinAsParticipantRelationship> findByTeamTeamId(UUID teamId);

}

