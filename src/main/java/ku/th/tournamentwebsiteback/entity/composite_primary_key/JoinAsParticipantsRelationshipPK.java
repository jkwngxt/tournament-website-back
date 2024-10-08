package ku.th.tournamentwebsiteback.entity.composite_primary_key;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class JoinAsParticipantsRelationshipPK implements Serializable {
    private String user_id;
    private UUID team_id;
    private UUID tournament_id;
}
