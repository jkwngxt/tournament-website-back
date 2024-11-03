package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class StaffRelationshipResponse {
    private UUID tournamentId;
    private String position;
    private String status;
    private UserProfileResponse user;
    private List<QualifierMatchProfileResponse> qualifierMatchJudges;
}
