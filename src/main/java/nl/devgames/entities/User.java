package nl.devgames.entities;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

/**
 * Created by Wouter on 3/5/2016.
 */
@NodeEntity
public class User extends Entity {
    private String username;
    private String githubUsername;
    private long points;
    private String password;
    private long linesWritten;
    private long linesCommented;
    private long duplicationLinesWritten;
    private long duplicationBlocksWritten;
    private long duplicationFilesWritten;
    private double debtCreated;
    private long dateTime;


    @Relationship(type = "ACHIEVEMENT_OF")
    Set<Achievement> achievements;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getLinesWritten() {
        return linesWritten;
    }

    public void setLinesWritten(long linesWritten) {
        this.linesWritten = linesWritten;
    }

    public long getLinesCommented() {
        return linesCommented;
    }

    public void setLinesCommented(long linesCommented) {
        this.linesCommented = linesCommented;
    }

    public long getDuplicationLinesWritten() {
        return duplicationLinesWritten;
    }

    public void setDuplicationLinesWritten(long duplicationLinesWritten) {
        this.duplicationLinesWritten = duplicationLinesWritten;
    }

    public long getDuplicationBlocksWritten() {
        return duplicationBlocksWritten;
    }

    public void setDuplicationBlocksWritten(long duplicationBlocksWritten) {
        this.duplicationBlocksWritten = duplicationBlocksWritten;
    }

    public long getDuplicationFilesWritten() {
        return duplicationFilesWritten;
    }

    public void setDuplicationFilesWritten(long duplicationFilesWritten) {
        this.duplicationFilesWritten = duplicationFilesWritten;
    }

    public double getDebtCreated() {
        return debtCreated;
    }

    public void setDebtCreated(double debtCreated) {
        this.debtCreated = debtCreated;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }

    public void addAchievements(Achievement achievement) {
        if(this.achievements == null){
            this.achievements = new HashSet<>();
        }
        this.achievements.add(achievement);
    }

    public long getDate() {
        return dateTime;
    }

    public void setDate(long dateTime) {
        this.dateTime = dateTime;
    }
}
