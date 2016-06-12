package nl.devgames.entities;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by draikos on 6/11/2016.
 */
@NodeEntity
public class PastCommits extends Entity {
    private String username;
    private long points;
    private long dateTime;

    public PastCommits(){

    }

    public PastCommits(Long id) {
        super(id);
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setDate(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Long getDate() {
        return dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}



