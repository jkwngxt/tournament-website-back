package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
@Data
@Entity
public class Judge {
    @EmbeddedId
    private JudgePK id;

    @ManyToOne
    @MapsId("joinAsStaffRelationshipId")
    private JoinAsStaffRelationship joinAsStaffRelationship;

    @ManyToOne
    @MapsId("lobbyId")
    @JoinColumn(name = "lobby_id")
    private QualifierMatch qualifierMatch;
}
