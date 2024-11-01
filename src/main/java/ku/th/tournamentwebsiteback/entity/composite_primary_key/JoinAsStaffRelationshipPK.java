package ku.th.tournamentwebsiteback.entity.composite_primary_key;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
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
public class JoinAsStaffRelationshipPK implements Serializable {
    private Integer userId;
    private UUID tournamentId;
}
