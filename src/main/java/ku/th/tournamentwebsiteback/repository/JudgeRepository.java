package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.JoinAsStaffRelationship;
import ku.th.tournamentwebsiteback.entity.Judge;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, JudgePK> {
    Judge findByJoinAsStaffRelationshipAndQualifierMatchLobbyId(JoinAsStaffRelationship joinAsStaffRelationship, UUID lobbyId);

    Judge findByJoinAsStaffRelationshipUserUserIdAndQualifierMatchLobbyId(Integer userId, UUID lobbyId);

}

