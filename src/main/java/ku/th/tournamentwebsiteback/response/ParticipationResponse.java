package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ParticipationResponse {
    Integer userId;
    UUID tournamentId;
    UUID teamId;
}
