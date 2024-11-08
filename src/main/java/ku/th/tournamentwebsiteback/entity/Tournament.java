package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
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

    @JsonManagedReference
    @OneToMany(mappedBy = "tournament")
    private List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @JsonManagedReference
    @OneToMany(mappedBy = "tournament")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @JsonManagedReference
    @OneToMany(mappedBy = "tournament")
    private List<QualifierMatch> qualifierMatches;

    public boolean isInTeamRegisPeriod() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = startTeamRegisDateTime.withZoneSameInstant(now.getZone());
        ZonedDateTime close = closeTeamRegisDateTime.withZoneSameInstant(now.getZone());
        return (start.isBefore(now) || start.isEqual(now)) && close.isAfter(now);
    }

}
