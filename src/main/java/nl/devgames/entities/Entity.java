package nl.devgames.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;

/**
 * Created by Wouter on 3/5/2016.
 */
public abstract class Entity {
    @GraphId
    Long id;
    public Entity(){
    }

    public Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

