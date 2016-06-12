package nl.tma.services.interfaces;

import nl.tma.entities.Project;
import nl.tma.entities.User;

import java.util.Set;

/**
 * Created by Wouter on 1/9/2016.
 */
public interface ProjectService {
    Project findProjectByProjectName(String projectName);
    public void save(Project project);
}
