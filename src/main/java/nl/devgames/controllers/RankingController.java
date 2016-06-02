package nl.devgames.controllers;

import nl.devgames.Application;
import nl.devgames.entities.Ranking;
import nl.devgames.entities.User;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class RankingController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @RequestMapping(value = "/ranking",method = RequestMethod.GET)
    public @ResponseBody List<Ranking> getRankingAll(){
        UserService userService = new UserServiceImpl();
        Set<User> allUsers = userService.getAllUsers();
        List<Ranking> rankingList = new ArrayList<>();
        for (User user : allUsers) {
            rankingList.add(new Ranking(user.getUsername(),user.getPoints()));
        }
        return rankingList;
    }

}
