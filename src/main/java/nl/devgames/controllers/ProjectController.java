package nl.devgames.controllers;

import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.entities.relationships.ProjectMember;
import nl.devgames.security.AuthToken;
import nl.devgames.security.SecurityFilter;
import nl.devgames.services.ProjectServiceImpl;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.ProjectService;
import nl.devgames.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class ProjectController {
    @RequestMapping(value = "/projects/{uuid}",method = RequestMethod.GET)
    public Project getProjectById(@PathVariable Long uuid, @RequestParam(value="password") String password) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        return projectService.find(uuid);
    }
    @RequestMapping(value = "/projects",method = RequestMethod.POST)
    public Project createNewProject(@RequestBody Project project, @RequestParam(value="password") String password) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        projectService.createOrUpdate(project);
        return project;
    }
    @RequestMapping(value = "/projects",method = RequestMethod.GET)
    public Set<Project> getProjectsOfUser(@RequestHeader(value = "Authorization") String authToken) {
        String username = AuthToken.getUsernameFromToken(authToken);
        UserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(username);
        return userService.findAllProjectsOfUser(user.getId());
    }
    @RequestMapping(value = "/projects/addUser",method = RequestMethod.POST)
    public Project addUserToProject(@RequestBody ProjectMember projectMember, @RequestParam(value="password") String password) {
        ProjectServiceImpl projectService = new ProjectServiceImpl();
        UserServiceImpl userService = new UserServiceImpl();
        Project project = projectService.find(projectMember.getProject().getId());
        User user = userService.find(projectMember.getUser().getId());
        if(project != null && user != null){
            project.addProjectMember(user);
            return projectService.createOrUpdate(project);
        }
        return null;
    }
}
