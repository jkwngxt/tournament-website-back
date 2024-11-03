package ku.th.tournamentwebsiteback.response;

import lombok.Data;

@Data
public class LoginResponse {
    String status;
    String token;
    UserProfileResponse userProfileResponse;

    public LoginResponse(String status, String token, UserProfileResponse userProfileResponse) {
        this.status = status;
        this.token = token;
        this.userProfileResponse = userProfileResponse;
    }
}
