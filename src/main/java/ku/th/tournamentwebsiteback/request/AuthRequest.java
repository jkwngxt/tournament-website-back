package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class AuthRequest {
    @NotBlank
    private Integer userId;
    @NotBlank
    private String username;
    @NotBlank
    private String country;
    @NotBlank
    private Integer rank;
    @NotBlank
    private String profileImageUrl;
    private UUID tournamentId;
}
