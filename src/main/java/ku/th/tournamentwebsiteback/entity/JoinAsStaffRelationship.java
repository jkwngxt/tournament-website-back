package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@IdClass(JoinAsStaffRelationshipPK.class)
public class JoinAsStaffRelationship {
    @Id
    private String user_id;
    @Id
    private UUID tournament_id;

    private String position;
    private String status;
}
