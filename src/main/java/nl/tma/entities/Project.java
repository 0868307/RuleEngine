package nl.tma.entities;

import nl.tma.entities.relationships.ProjectMember;
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

    @Relationship(type = "PROJECT_MEMBER", direction = "OUTGOING")
    Set<ProjectMember> projectMembers;

    private long lines;
    private long linesCommented;
    private long duplicationLines;
    private long duplicationBlocks;
    private long duplicationFiles;
    private double complexity;
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

    public Set<ProjectMember> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }
    public void addProjectMember(User user){
        if(projectMembers == null){
            projectMembers = new HashSet<>();
        }
        ProjectMember projectMember = new ProjectMember(user,this,0);
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

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public ProjectMember getProjectMember(User user) {
        if(projectMembers != null){
            for(ProjectMember projectMember : projectMembers){
                if(projectMember.getUser().equals(user)){
                    return projectMember;
                }
            }
        }
        return null;
    }

    public void updateProjectMember(ProjectMember projectMember) {
        if(projectMembers != null){
            projectMembers.remove(projectMember);
            projectMembers.add(projectMember);
        }
    }
}
