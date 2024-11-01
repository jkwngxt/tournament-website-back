package ku.th.tournamentwebsiteback.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class TeamDTO {
    private UUID teamId;
    private String teamName;
}
