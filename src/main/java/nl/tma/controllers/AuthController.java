package nl.tma.controllers;

import nl.tma.security.AuthToken;
import nl.tma.security.SecurityFilter;
import nl.tma.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wouter on 3/17/2016.
 */
@Controller
public class AuthController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public @ResponseBody String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.validateUser(username,password)){
            return AuthToken.generate(username);
        }else{
            throw new SecurityException("Incorrect login info");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public void logout(@RequestHeader(value = SecurityFilter.AUTHORIZATION) String authToken){
        AuthToken.invalidate(authToken);
    }
}
