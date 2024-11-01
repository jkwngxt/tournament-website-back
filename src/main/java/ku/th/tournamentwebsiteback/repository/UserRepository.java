package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
