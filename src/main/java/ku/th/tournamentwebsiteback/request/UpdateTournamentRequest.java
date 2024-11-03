package ku.th.tournamentwebsiteback.request;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UpdateTournamentRequest {
    private String title;
    private String description;
    private ZonedDateTime startQualifierDateTime;
    private ZonedDateTime endDateTime;
    private ZonedDateTime startStaffRegisDateTime;
    private ZonedDateTime startTeamRegisDateTime;
    private ZonedDateTime closeTeamRegisDateTime;
    private Integer teamMemberAmount;
    private Boolean isAcceptInternationalStaff;
    private Integer expectedStaff;
}
