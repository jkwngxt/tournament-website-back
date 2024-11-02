package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QualifierMatchDetailResponse {
    private UUID lobbyId;
    private String lobbyName;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;
    private List<UserProfileResponse> judges;
    private List<TeamProfileResponse> teams;
}
