package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;
@Data
@Entity
public class Tournament {
    @Id
    private UUID tournament_id;
    private String title;
    private String description;
    private ZonedDateTime start_qualifier_date_time;
    private ZonedDateTime end_date_time;
    private ZonedDateTime start_staff_regis_date_time;
    private ZonedDateTime start_team_regis_date_time;
    private ZonedDateTime close_team_regis_date_time;
    private int team_member_amount;
    private boolean is_accept_international_staff;
    private int expected_staff;
}
