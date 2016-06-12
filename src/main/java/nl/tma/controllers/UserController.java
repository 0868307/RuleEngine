package nl.tma.controllers;

import nl.tma.entities.Achievement;
import nl.tma.entities.User;
import nl.tma.security.AuthToken;
import nl.tma.security.SecurityFilter;
import nl.tma.services.UserServiceImpl;
import nl.tma.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {
    @RequestMapping("/whoami")
    public String whoAmI(@RequestHeader(value = SecurityFilter.AUTHORIZATION) String authToken) {
        return AuthToken.getUsernameFromToken(authToken);
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Long uuid, @RequestHeader(value = SecurityFilter.AUTHORIZATION) String auth_token) {
        if (AuthToken.checkToken(auth_token) == true) {
            return new UserServiceImpl().find(uuid);
        } else{
            return null;
        }
    }
    @RequestMapping(value = "/user/{uuid}",method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long uuid,@RequestBody User user) {

            if(user.getId() == uuid) {
            UserService userService = new UserServiceImpl();
            userService.save(user);
            return user;
        }
        return null;
    }
    @RequestMapping(value = "/user/achievements", method = RequestMethod.GET)
    public Set<Achievement> getAchievements(@RequestHeader(value = SecurityFilter.AUTHORIZATION) String authToken){
        String username = AuthToken.getUsernameFromToken(authToken);
        UserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(username);
        return user.getAchievements();
    }
}
