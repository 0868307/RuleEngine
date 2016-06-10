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

    @Relationship(type = "ISSUE_OF")
    Set<Issue> issues;

    @Relationship(type = "PROJECT_MEMBER")
    Set<User> projectMembers;

    private Long lines;
    private Long linesCommented;
    private Long duplicationLines;
    private Long duplicationBlocks;
    private Long duplicationFiles;
    private Double duplicationLinesDensity;
    private Double debt;


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

    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public Long getLines() {
        return lines;
    }

    public void setLines(Long lines) {
        this.lines = lines;
    }

    public Long getLinesCommented() {
        return linesCommented;
    }

    public void setLinesCommented(Long linesCommented) {
        this.linesCommented = linesCommented;
    }

    public Long getDuplicationLines() {
        return duplicationLines;
    }

    public void setDuplicationLines(Long duplicationLines) {
        this.duplicationLines = duplicationLines;
    }

    public Double getDuplicationLinesDensity() {
        return duplicationLinesDensity;
    }

    public void setDuplicationLinesDensity(Double duplicationLinesDensity) {
        this.duplicationLinesDensity = duplicationLinesDensity;
    }

    public Long getDuplicationBlocks() {
        return duplicationBlocks;
    }

    public void setDuplicationBlocks(Long duplicationBlocks) {
        this.duplicationBlocks = duplicationBlocks;
    }

    public Long getDuplicationFiles() {
        return duplicationFiles;
    }

    public void setDuplicationFiles(Long duplicationFiles) {
        this.duplicationFiles = duplicationFiles;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }
}
