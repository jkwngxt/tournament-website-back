package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.JoinAsParticipantRelationship;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JoinAsParticipantRepository extends JpaRepository<JoinAsParticipantRelationship, JoinAsParticipantsRelationshipPK> {
    boolean existsByUserAndTournament(User user, Tournament tournament);
}

