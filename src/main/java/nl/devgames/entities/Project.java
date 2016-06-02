package nl.devgames.entities;

import nl.devgames.entities.relationships.ProjectMember;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Wouter on 3/5/2016.
 */
@NodeEntity
public class Project extends Entity {
    private String name;

    @Relationship(type = "PROJECT_MEMBER")
    Set<User> projectMembers;

    public Project() {
    }

    public Project(Long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<User> projectMembers) {
        this.projectMembers = projectMembers;
    }
    public void addProjectMember(User projectMember){
        if(projectMembers == null){
            projectMembers = new HashSet<>();
        }
        this.projectMembers.add(projectMember);
    }
}
