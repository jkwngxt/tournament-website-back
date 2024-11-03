package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import lombok.Data;

@Data
@Entity
public class Judge {
    @EmbeddedId
    private JudgePK id;

    @JsonBackReference
    @ManyToOne
    @MapsId("joinAsStaffRelationshipId")
    private JoinAsStaffRelationship joinAsStaffRelationship;

    @JsonBackReference
    @ManyToOne
    @MapsId("lobbyId")
    @JoinColumn(name = "lobby_id")
    private QualifierMatch qualifierMatch;
}
