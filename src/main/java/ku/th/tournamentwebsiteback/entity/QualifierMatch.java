package ku.th.tournamentwebsiteback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Entity
public class QualifierMatch {
    @Id
    private UUID lobbyId;
    private String lobbyName;
    private ZonedDateTime startLobbyDateTime;
    private ZonedDateTime closeLobbyDateTime;

    @OneToMany(mappedBy = "qualifierMatch")
    List<Team> teams;

    @OneToMany(mappedBy = "qualifierMatch")
    List<Judge> judges;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

}
