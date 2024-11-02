package ku.th.tournamentwebsiteback.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TeamProfileDTO {
    private UUID teamId;
    private String teamName;
    private String profileImage;
}
