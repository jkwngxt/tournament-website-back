package ku.th.tournamentwebsiteback.entity;

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

    @OneToMany(mappedBy = "team")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private QualifierMatch qualifierMatch;

    @OneToOne
    @JoinColumn(name = "captain_user_id")
    private User user;
}
