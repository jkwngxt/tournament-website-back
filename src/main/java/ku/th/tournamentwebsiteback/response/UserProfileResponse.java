package ku.th.tournamentwebsiteback.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Integer userId;
    private String username;
    private String country;
    private Integer rank;
    private String profileImageUrl;
}
