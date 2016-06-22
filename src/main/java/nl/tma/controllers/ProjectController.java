package nl.tma.controllers;

import nl.tma.entities.Issue;
import nl.tma.entities.Project;
import nl.tma.entities.User;
import nl.tma.entities.relationships.ProjectMember;
import nl.tma.security.AuthToken;
import nl.tma.security.SecurityFilter;
import nl.tma.services.ProjectServiceImpl;
import nl.tma.services.UserServiceImpl;
import nl.tma.services.interfaces.ProjectService;
import nl.tma.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class ProjectController {
    @RequestMapping(value = "/projects/{uuid}",method = RequestMethod.GET)
    public Project getProjectById(@PathVariable Long uuid) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        return projectService.find(uuid);
    }
    @RequestMapping(value = "/projects/{name}",method = RequestMethod.GET)
    public Project getProjectByName(@PathVariable String projectName) {
        ProjectService projectService = new ProjectServiceImpl();
        return projectService.findProjectByProjectName(projectName);
    }
    @RequestMapping(value = "/projects",method = RequestMethod.GET)
    public Set<Project> getProjectsOfUser(@RequestHeader(value = SecurityFilter.AUTHORIZATION) String authToken) {
        String username = AuthToken.getUsernameFromToken(authToken);
        UserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(username);
        return userService.findAllProjectsOfUser(user.getId());
    }
    @RequestMapping(value = "/projects/{uuid}/issues",method = RequestMethod.GET)
    public Set<Issue> getProjectIssuesByID(@PathVariable Long uuid) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        Project project = projectService.find(uuid);
        return project.getIssues();
    }
    @RequestMapping(value = "/projects/{uuid}/members",method = RequestMethod.GET)
    public Set<ProjectMember> getProjectMembersByProjectId(@PathVariable Long uuid) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        Project project = projectService.find(uuid);
        return project.getProjectMembers();
    }
}
