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
    private UUID lobbyId;
    private String status;

    @OneToMany(mappedBy = "team")
    List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @ManyToOne
    private QualifierMatch qualifierMatch;
}
