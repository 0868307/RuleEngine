package nl.devgames.controllers;

import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.services.CommitServiceImpl;
import nl.devgames.services.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class CommitController {
    @RequestMapping(value = "/commits/{uuid}",method = RequestMethod.GET)
    public Commit getProjectById(@PathVariable Long uuid) {
        CommitServiceImpl commitService = new CommitServiceImpl();
        return commitService.find(uuid);
    }
    @RequestMapping(value = "/commits",method = RequestMethod.POST)
    public Commit createNewProject(@RequestBody Commit commit) {
        CommitServiceImpl commitService = new CommitServiceImpl();
        return commitService.createOrUpdate(commit);
    }
}
