package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UpdateTournamentRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private ZonedDateTime startQualifierDateTime;
    @NotBlank
    private ZonedDateTime endDateTime;

    private ZonedDateTime startStaffRegisDateTime;
    private ZonedDateTime startTeamRegisDateTime;
    private ZonedDateTime closeTeamRegisDateTime;
    private int teamMemberAmount;
    private boolean isAcceptInternationalStaff;
    private int expectedStaff;
}
