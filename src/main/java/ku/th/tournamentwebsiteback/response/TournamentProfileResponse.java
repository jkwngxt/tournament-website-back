package ku.th.tournamentwebsiteback.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class TournamentProfileDTO {
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
}
