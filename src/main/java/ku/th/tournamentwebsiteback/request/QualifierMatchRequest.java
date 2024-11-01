package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class QualifierMatchRequest {
    @NotBlank
    private String lobbyName;
    @NotBlank
    private ZonedDateTime startLobbyDateTime;
    @NotBlank
    private ZonedDateTime closeLobbyDateTime;
}
