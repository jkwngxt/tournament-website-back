package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class TournamentRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private ZonedDateTime startQualifierDateTime;
    @NotBlank
    private ZonedDateTime endDateTime;

}

