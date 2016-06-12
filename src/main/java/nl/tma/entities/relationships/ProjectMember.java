package nl.tma.entities.relationships;

import nl.tma.entities.Project;
import nl.tma.entities.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Created by Wouter on 3/22/2016.
 */
@RelationshipEntity(type = "PROJECT_MEMBER")
public class ProjectMember {
    private Long id;

    @StartNode
    private User user;

    @EndNode
    private Project project;

    public ProjectMember() {
    }

    public ProjectMember(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
