package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    Team findByUserUserId(Integer user_id);
}
