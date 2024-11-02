package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TeamDetailResponse {
    private UUID teamId;
    private String teamName;
    private String profileImage;
    private UUID tournamentId;
    private String status;
    private UserProfileResponse captain;
    private List<UserProfileResponse> members;
    private QualifierMatchProfileResponse qualifierMatch;
}

