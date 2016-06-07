package nl.devgames.controllers;

import nl.devgames.security.AuthToken;
import nl.devgames.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Wouter on 3/17/2016.
 */
@Controller
public class AuthController {
    //TODO: add Token class
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {

        UserServiceImpl userService = new UserServiceImpl();
        if(userService.validateUser(username,password)){
            return AuthToken.generate(username);
        }else{
            throw new SecurityException("Incorrect login info");
        }
    }
    public void logout(){
        //TODO INVALIDATE TOKEN
        throw new UnsupportedOperationException("This will invalidate your authtoken");
    }

}
