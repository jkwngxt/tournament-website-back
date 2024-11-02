package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TeamProfileResponse {
    private UUID teamId;
    private String teamName;
    private String profileImage;
}
