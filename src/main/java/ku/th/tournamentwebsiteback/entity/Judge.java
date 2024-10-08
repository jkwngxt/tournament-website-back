package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import ku.th.tournamentwebsiteback.entity.composite_primary_key.JudgePK;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
@Data
@Entity
@IdClass(JudgePK.class)
public class Judge implements Serializable {
    @Id
    private UUID lobby_id;
    @Id
    private String user_id;
    @Id
    private UUID tournament_id;
}
