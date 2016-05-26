package nl.devgames.entities;

/**
 * Created by Wouter on 5/26/2016.
 */
public class Ranking {
    private String username;
    private Long points;

    public Ranking(String username, Long points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
