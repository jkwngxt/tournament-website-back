package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Users {
    @Id
    private Integer userId;
    private String username;
    private String country;
    private Integer rank;
    private String profileImageUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<JoinAsStaffRelationship> joinAsStaffRelationships;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<JoinAsParticipantRelationship> joinAsParticipantRelationships;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", optional = true)
    private Admin admin;

    @JsonManagedReference
    @OneToOne(mappedBy = "captain", optional = true)
    private Team team;

}
