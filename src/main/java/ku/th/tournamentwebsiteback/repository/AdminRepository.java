package ku.th.tournamentwebsiteback.repository;

import ku.th.tournamentwebsiteback.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
