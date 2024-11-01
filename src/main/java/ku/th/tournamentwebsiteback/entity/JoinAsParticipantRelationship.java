package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsParticipantsRelationshipPK;
import lombok.Data;

@Data
@Entity
public class JoinAsParticipantRelationship {
    @EmbeddedId
    private JoinAsParticipantsRelationshipPK id;

    @JsonBackReference
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    @MapsId("tournamentId")
    private Tournament tournament;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "team_id")
    @MapsId("teamId")
    private Team team;
}
