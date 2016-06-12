package nl.devgames.controllers;

import nl.devgames.security.AuthToken;
import nl.devgames.security.SecurityFilter;
import nl.devgames.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wouter on 3/17/2016.
 */
@Controller
public class AuthController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public @ResponseBody String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.validateUser(username,password) == true){
            String a_token = AuthToken.generate(username);
            SecurityFilter.auth_token = a_token;
            return AuthToken.generate(username);
        }else{
            throw new SecurityException("Incorrect login info");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public void logout(@RequestHeader(value = "Authorization") String authToken){
        AuthToken.invalidate(authToken);
    }
}
