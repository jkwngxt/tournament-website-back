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
    private Integer userId;
    private String username;
    private String country;
    private Integer rank;
    private String profileImageUrl;

    @OneToMany(mappedBy = "user")
    List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @OneToMany(mappedBy = "user")
    List<JoinAsParticipantRelationship> joinAsParticipantRelationships;
}
