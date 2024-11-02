package ku.th.tournamentwebsiteback.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateStaffRequest {
    @NotBlank
    String status;
}
