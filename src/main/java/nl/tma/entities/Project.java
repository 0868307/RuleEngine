package nl.tma.entities;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
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

    private long lines;
    private long linesCommented;
    private long duplicationLines;
    private long duplicationBlocks;
    private long duplicationFiles;
    private double debt;


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
        if(issues ==  null){
            issues = new HashSet<>();
        }
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public long getLines() {
        return lines;
    }

    public void setLines(long lines) {
        this.lines = lines;
    }

    public long getLinesCommented() {
        return linesCommented;
    }

    public void setLinesCommented(long linesCommented) {
        this.linesCommented = linesCommented;
    }

    public long getDuplicationLines() {
        return duplicationLines;
    }

    public void setDuplicationLines(long duplicationLines) {
        this.duplicationLines = duplicationLines;
    }

    public long getDuplicationBlocks() {
        return duplicationBlocks;
    }

    public void setDuplicationBlocks(long duplicationBlocks) {
        this.duplicationBlocks = duplicationBlocks;
    }

    public long getDuplicationFiles() {
        return duplicationFiles;
    }

    public void setDuplicationFiles(long duplicationFiles) {
        this.duplicationFiles = duplicationFiles;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }
}
