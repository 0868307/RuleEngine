package nl.tma.services.interfaces;

import nl.tma.entities.Project;

/**
 * Created by Wouter on 1/9/2016.
 */
public interface ProjectService {
    Project findProjectByProjectName(String projectName);
    public void save(Project project);
}
