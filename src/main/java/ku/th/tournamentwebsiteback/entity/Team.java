package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
public class Team {
    @Id
    private UUID team_id;
    private String captain_user_id;
    private String team_name;
    private String profile_image;
    private UUID lobby_id;
    private String status;
}
