package nl.devgames.entities;

import nl.devgames.entities.relationships.CommittedBy;
import nl.devgames.entities.relationships.CommittedTo;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * Created by Wouter on 3/5/2016.
 */
@NodeEntity
public class Commit extends Entity {
    private String hash;
    private String description;
    private String branch;
    private int filesChanged;
    private long timestamp;
    @Relationship(type = "COMMITTED_BY")
    private User committedBy;
    @Relationship(type = "COMMITTED_TO")
    private Project committedTo;
    public Commit() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getFilesChanged() {
        return filesChanged;
    }

    public void setFilesChanged(int filesChanged) {
        this.filesChanged = filesChanged;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public User getCommittedBy() {
        return committedBy;
    }

    public void setCommittedBy(User committedBy) {
        this.committedBy = committedBy;
    }

    public Project getCommittedTo() {
        return committedTo;
    }

    public void setCommittedTo(Project committedTo) {
        this.committedTo = committedTo;
    }
}
