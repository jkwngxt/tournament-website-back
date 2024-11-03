package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.JoinAsStaffRelationship;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.Users;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JoinAsStaffRepository extends JpaRepository<JoinAsStaffRelationship, JoinAsStaffRelationshipPK> {
    boolean existsByUserAndTournament(Users user, Tournament tournament);

    JoinAsStaffRelationship findByUserAndTournament(Users user, Tournament tournament);

    List<JoinAsStaffRelationship> findByTournamentTournamentId(UUID tournamentId);

    List<JoinAsStaffRelationship> findByUserUserId(Integer userId);

    JoinAsStaffRelationship findByUserUserIdAndTournamentTournamentId(Integer userId, UUID tournamentId);
}
