package nl.tma.entities;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by Wouter on 6/10/2016.
 */
@NodeEntity
public class Achievement extends Entity {
    private String title;
    private String reason;

    public Achievement() {
    }

    public Achievement(Long id) {
        super(id);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
