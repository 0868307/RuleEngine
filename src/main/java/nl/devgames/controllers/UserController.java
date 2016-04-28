package nl.devgames.controllers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.services.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @RequestMapping("/whoami")
    public User whoAmI(@RequestParam(value="name", defaultValue="World") String name) {
        throw new UnsupportedOperationException("This will return the current logged in user");
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Long uuid) {
        return new UserServiceImpl().find(uuid);
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long uuid,@RequestBody User user) {
        throw new UnsupportedOperationException("this will update the user of uuid");
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
}
