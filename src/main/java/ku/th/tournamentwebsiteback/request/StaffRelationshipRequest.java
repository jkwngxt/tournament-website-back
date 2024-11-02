package ku.th.tournamentwebsiteback.request;

import lombok.Data;

import java.util.UUID;

@Data
public class StaffRelationshipRequest {
    private Integer userId;
    private UUID tournamentId;
    private String position;
}
