package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.JoinAsStaffRelationship;
import ku.th.tournamentwebsiteback.entity.Team;
import ku.th.tournamentwebsiteback.entity.Tournament;
import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JoinAsStaffRepository extends JpaRepository<Team, UUID> {
    boolean existsByUserAndTournament(User user, Tournament tournament);

    JoinAsStaffRelationship findByUserAndTournament(User user, Tournament tournament);
}
