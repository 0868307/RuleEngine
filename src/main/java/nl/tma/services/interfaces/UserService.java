package nl.tma.services.interfaces;

import nl.tma.entities.Project;
import nl.tma.entities.User;

import java.util.Set;

/**
 * Created by Wouter on 1/9/2016.
 */
public interface UserService {
    public boolean validateUser(String username,String password);

    public Set<Project> findAllProjectsOfUser(Long id);
    public User findUserByUsername(String username);
    public User findUserByGithubname(String githubname);
    public Set<User> getAllUsers();

    public void save(User user);
}
