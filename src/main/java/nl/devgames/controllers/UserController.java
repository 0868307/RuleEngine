package nl.devgames.controllers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import nl.devgames.entities.PastCommits;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.security.AuthToken;
import nl.devgames.security.SecurityFilter;
import nl.devgames.services.PastCommitImpl;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.UserService;
import org.omg.CORBA.Request;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @RequestMapping("/whoami")
    public String whoAmI(@RequestHeader(value = "Authorization") String authToken) {
        return AuthToken.getUsernameFromToken(authToken);
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Long uuid, @RequestHeader(value="Authorization") String token) {
        if (AuthToken.checkToken(token) == true) {

            return new UserServiceImpl().find(uuid);
        }
        else{

            return null;
        }
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long uuid,@RequestBody User user ) {

            if (user.getId() == uuid) {
                UserService userService = new UserServiceImpl();
                userService.save(user);
                return user;
            }


        return null;
    }
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public User createNewUser(@RequestBody User user) {
        UserServiceImpl userService = new UserServiceImpl();
        return userService.createOrUpdate(user);
    }
    @RequestMapping(value = "/user/{uuid}/projects",method = RequestMethod.GET)
    public Set<Project> createNewUser(@PathVariable Long uuid) {
        UserServiceImpl userService = new UserServiceImpl();
        return userService.findAllProjectsOfUser(uuid);
    }
    @RequestMapping(value = "/usertest",method = RequestMethod.POST)
    public PastCommits test(@RequestBody PastCommits pastCommits){
        PastCommitImpl pastCommit = new PastCommitImpl();
        return pastCommit.createOrUpdate(pastCommits);
    }
}
