package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @OneToMany(mappedBy = "user")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @OneToOne(mappedBy = "user", optional = true)
    private Admin admin;

    @OneToOne(mappedBy = "user", optional = true)
    private Team team;

}
