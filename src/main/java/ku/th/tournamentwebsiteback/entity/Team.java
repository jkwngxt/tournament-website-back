package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
@Entity
public class Team {
    @Id
    private UUID teamId;
    private String captainUserId;
    private String teamName;
    private String profileImage;
    private String status;

    @OneToMany(mappedBy = "team")
    List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private QualifierMatch qualifierMatch;
}
