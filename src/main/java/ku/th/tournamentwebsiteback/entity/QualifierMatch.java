package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;
@Data
@Entity
public class QualifierMatch {
    @Id
    private UUID lobby_id;
    private String referee_user_id;
    private String lobby_name;
    private ZonedDateTime start_lobby_date_time;
    private ZonedDateTime close_lobby_date_time;
    private String tournament_id;

}
