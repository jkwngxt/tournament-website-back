package ku.th.tournamentwebsiteback.DTO;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QualifierMatchDTO {
    private UUID lobbyId;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;
    private List<JudgeDTO> judges;
    private List<TeamDTO> teams;
}
