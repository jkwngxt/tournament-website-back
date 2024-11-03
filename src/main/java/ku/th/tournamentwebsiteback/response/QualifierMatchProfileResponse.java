package ku.th.tournamentwebsiteback.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class QualifierMatchProfileResponse {
    private UUID lobbyId;
    private String lobbyName;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;
}
