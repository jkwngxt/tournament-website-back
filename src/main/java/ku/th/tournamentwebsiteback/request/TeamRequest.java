package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequest {
    //    @NotBlank
//    private String captainUserId;
    @NotBlank
    private String teamName;
    @NotBlank
    private String profileImage;
}
