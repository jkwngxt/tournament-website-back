package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JoinAsStaffRelationshipPK;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class JoinAsStaffRelationship {
    @EmbeddedId
    private JoinAsStaffRelationshipPK id;

    private String position;
    private String status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @JsonBackReference
    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @JsonManagedReference
    @OneToMany(mappedBy = "joinAsStaffRelationship")
    private List<Judge> judges;
}
