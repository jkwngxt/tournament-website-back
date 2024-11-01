package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Entity
public class Tournament {
    @Id
    private UUID tournamentId;
    private String title;
    private String description;
    private ZonedDateTime startQualifierDateTime;
    private ZonedDateTime endDateTime;
    private ZonedDateTime startStaffRegisDateTime;
    private ZonedDateTime startTeamRegisDateTime;
    private ZonedDateTime closeTeamRegisDateTime;
    private int teamMemberAmount;
    private boolean isAcceptInternationalStaff;
    private int expectedStaff;

    @OneToMany(mappedBy = "tournament")
    private List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @OneToMany(mappedBy = "tournament")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @OneToMany(mappedBy = "tournament")
    private List<QualifierMatch> qualifierMatches;
}
