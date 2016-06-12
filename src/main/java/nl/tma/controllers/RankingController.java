package nl.tma.controllers;

import nl.tma.entities.Ranking;
import nl.tma.entities.User;
import nl.tma.services.UserServiceImpl;
import nl.tma.services.interfaces.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class RankingController {

    //private static final Logger logger = LoggerFactory.getLogger(Application.class);
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
