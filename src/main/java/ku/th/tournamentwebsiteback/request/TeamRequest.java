package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TeamRequest {
    @NotBlank
    private String teamName;
    @NotBlank
    private String profileImage;
    @NotBlank
    private Integer captainUserId;
    @NotBlank
    private List<Integer> memberIdList;
    @NotBlank
    private UUID tournamentId;
}
