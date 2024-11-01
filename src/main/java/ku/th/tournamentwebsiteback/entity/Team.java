package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue
    private UUID teamId;
    private String teamName;
    private String profileImage;
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "team")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private QualifierMatch qualifierMatch;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "captain_user_id")
    private User user;
}
