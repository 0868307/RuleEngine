package nl.devgames.entities.relationships;

import nl.devgames.entities.Commit;
import nl.devgames.entities.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Created by Wouter on 3/22/2016.
 */
@RelationshipEntity(type = "COMMITTED_BY")
public class CommittedBy {
    private Long id;

    @StartNode
    private Commit commit;

    @EndNode
    private User user;

    public CommittedBy() {
    }

    public CommittedBy(User user, Commit commit) {
        this.user = user;
        this.commit = commit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
