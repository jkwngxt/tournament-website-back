package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateTeamRequest {
    @NotBlank
    private String status;
}
