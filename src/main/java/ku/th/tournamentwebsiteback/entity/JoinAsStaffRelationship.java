package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
@Entity
public class JoinAsStaffRelationship {
    @EmbeddedId
    private JoinAsStaffRelationshipPK id;

    private String position;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(mappedBy = "joinAsStaffRelationship")
    private List<Judge> judges;
}
