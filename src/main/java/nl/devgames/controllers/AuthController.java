package nl.devgames.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Wouter on 3/17/2016.
 */
@Controller
public class AuthController {
    //TODO: add Token class
    @RequestMapping("/login")
    public boolean login(@RequestParam(value="username") String username,@RequestParam(value="password") String password) {
        throw new UnsupportedOperationException("This will return a authtoken");
    }
    public void logout(){
        //TODO INVALIDATE TOKEN
        throw new UnsupportedOperationException("This will invalidate your authtoken");
    }
}
