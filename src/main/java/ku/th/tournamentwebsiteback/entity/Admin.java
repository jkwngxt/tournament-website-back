package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Admin {
    @Id
    private Integer userId;

    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
