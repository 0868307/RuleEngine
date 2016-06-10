package nl.devgames.entities;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Wouter on 3/5/2016.
 */
@NodeEntity
public class User extends Entity {
    private String username;
    private String githubUsername;
    private long points;
    private String password;
    private Long linesWritten;
    private Long linesCommented;
    private Long duplicationLinesWritten;
    private Long duplicationBlocksWritten;
    private Long duplicationFilesWritten;
    private Double debtCreated;

    List<Achievement> achievements;

    public User() {
    }

    public User(Long id) {
        super(id);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Long getLinesWritten() {
        return linesWritten;
    }

    public void setLinesWritten(Long linesWritten) {
        this.linesWritten = linesWritten;
    }

    public Long getLinesCommented() {
        return linesCommented;
    }

    public void setLinesCommented(Long linesCommented) {
        this.linesCommented = linesCommented;
    }

    public Long getDuplicationLinesWritten() {
        return duplicationLinesWritten;
    }

    public void setDuplicationLinesWritten(Long duplicationLinesWritten) {
        this.duplicationLinesWritten = duplicationLinesWritten;
    }

    public Long getDuplicationBlocksWritten() {
        return duplicationBlocksWritten;
    }

    public void setDuplicationBlocksWritten(Long duplicationBlocksWritten) {
        this.duplicationBlocksWritten = duplicationBlocksWritten;
    }

    public Long getDuplicationFilesWritten() {
        return duplicationFilesWritten;
    }

    public void setDuplicationFilesWritten(Long duplicationFilesWritten) {
        this.duplicationFilesWritten = duplicationFilesWritten;
    }

    public Double getDebtCreated() {
        return debtCreated;
    }

    public void setDebtCreated(Double debtCreated) {
        this.debtCreated = debtCreated;
    }
}
