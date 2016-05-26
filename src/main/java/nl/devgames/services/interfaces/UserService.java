package nl.devgames.services.interfaces;

import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;

import java.util.List;
import java.util.Set;

/**
 * Created by Wouter on 1/9/2016.
 */
public interface UserService {
    public boolean validateUser(String username,String password);

    public Set<Project> findAllProjectsOfUser(Long id);
    public Set<Commit> findAllCommitsOfUser(User user);
    public User findUserByUsername(String username);
    public User findUserByGithubname(String githubname);
    public Set<User> getAllUsers();
}
