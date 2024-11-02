package ku.th.tournamentwebsiteback.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TeamDetailDTO {
        private UUID teamId;
        private String teamName;
        private String profileImage;
        private UUID tournamentId;
        private String status;
        private UserProfileDTO captain;
        private List<UserProfileDTO> members;
        private QualifierMatchProfileDTO qualifierMatch;
}

