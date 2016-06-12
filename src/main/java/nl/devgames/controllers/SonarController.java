package nl.devgames.controllers;

import nl.devgames.Application;
import nl.devgames.handlers.SonarReportHandler;
import org.neo4j.ogm.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class SonarController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @RequestMapping(value = "/sonar",method = RequestMethod.POST)
    public boolean saveSonarInfo(@RequestBody String jsonAsString , @RequestHeader(value = "Authorization") String auth_token) {
        try {
             new SonarReportHandler().processSonarReport(jsonAsString);
            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
