package ku.th.tournamentwebsiteback.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Integer userId;
    private String username;
    private String country;
    private Integer rank;
    private String profileImageUrl;
}
