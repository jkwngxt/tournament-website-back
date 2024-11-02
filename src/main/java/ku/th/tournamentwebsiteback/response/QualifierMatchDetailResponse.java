package ku.th.tournamentwebsiteback.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QualifierMatchDetailDTO {
    private UUID lobbyId;
    private String lobbyName;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;
    private List<JudgeProfileResponse> judges;
    private List<TeamProfileDTO> teams;
}
