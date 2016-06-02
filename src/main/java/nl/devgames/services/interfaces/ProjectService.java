package nl.devgames.services.interfaces;

import nl.devgames.entities.Project;
import nl.devgames.entities.User;

import java.util.Set;

/**
 * Created by Wouter on 1/9/2016.
 */
public interface ProjectService {
    public Set<User> getUsersOfProject(String project);

    Project findProjectByProjectName(String projectName);
    public void save(Project project);
}
