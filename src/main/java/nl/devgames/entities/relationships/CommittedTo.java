package nl.devgames.entities.relationships;

import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Created by Wouter on 3/22/2016.
 */
@RelationshipEntity(type = "COMMITTED_TO")
public class CommittedTo {
    private Long id;

    @StartNode
    private Commit commit;

    @EndNode
    private Project project;

    public CommittedTo() {
    }

    public CommittedTo(Commit commit, Project project) {
        this.commit = commit;
        this.project = project;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
