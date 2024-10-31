package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User {
    @Id
    private String userId;
    private String username;
    private String country;
    private int rank;

    @OneToMany(mappedBy = "user")
    List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @OneToMany(mappedBy = "user")
    List<JoinAsParticipantRelationship> joinAsParticipantRelationships;
}
