package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    private String user_id;
    private String username;
    private String country;
    private int Rank;
}
