package ku.th.tournamentwebsiteback.entity.composite_primary_key;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class JudgePK implements Serializable {
    private JoinAsStaffRelationshipPK joinAsStaffRelationshipId;
    private UUID lobbyId;
}
