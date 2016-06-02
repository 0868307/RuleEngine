package nl.devgames.controllers;

import nl.devgames.Application;
import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.handlers.SonarReportHandler;
import nl.devgames.services.CommitServiceImpl;
import nl.devgames.services.UserServiceImpl;
import org.neo4j.ogm.json.JSONArray;
import org.neo4j.ogm.json.JSONException;
import org.neo4j.ogm.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class SonarController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @RequestMapping(value = "/sonar",method = RequestMethod.POST)
    public void saveSonarInfo(@RequestBody String jsonAsString) {
        try {
             new SonarReportHandler().processSonarReport(jsonAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
