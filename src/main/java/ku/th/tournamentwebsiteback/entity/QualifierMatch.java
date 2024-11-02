package ku.th.tournamentwebsiteback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class QualifierMatch {
    @Id
    @GeneratedValue
    private UUID lobbyId;
    private String lobbyName;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "qualifierMatch")
    private List<Team> teams;

    @JsonManagedReference
    @OneToMany(mappedBy = "qualifierMatch")
    private List<Judge> judges = new ArrayList<>();;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

}
