package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@IdClass(JoinAsParticipantsRelationshipPK.class)
public class JoinAsParticipantsRelationship {
    @Id
    private String user_id;
    @Id
    private UUID team_id;
    @Id
    private UUID tournament_id;
}
