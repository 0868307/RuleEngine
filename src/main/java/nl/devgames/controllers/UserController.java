package nl.devgames.controllers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import nl.devgames.entities.Achievement;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.security.AuthToken;
import nl.devgames.security.SecurityFilter;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @RequestMapping("/whoami")
    public String whoAmI(@RequestHeader(SecurityFilter.AUTHTOKEN) String authToken) {
        return AuthToken.getUsernameFromToken(authToken);
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Long uuid) {
        return new UserServiceImpl().find(uuid);
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long uuid,@RequestBody User user) {
        if(user.getId() == uuid){
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
    @RequestMapping(value = "/user/achievements", method = RequestMethod.GET)
    public Set<Achievement> getAchievements(@RequestHeader(SecurityFilter.AUTHTOKEN) String authToken){
        String username = AuthToken.getUsernameFromToken(authToken);
        UserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(username);
        return user.getAchievements();
    }
}
