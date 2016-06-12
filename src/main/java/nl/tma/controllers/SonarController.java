package nl.tma.controllers;

import nl.tma.Application;
import nl.tma.handlers.SonarReportHandler;
import org.neo4j.ogm.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class SonarController {
    @RequestMapping(value = "/sonar",method = RequestMethod.POST)
    public boolean saveSonarInfo(@RequestBody String jsonAsString) {
        try {
             new SonarReportHandler().processSonarReport(jsonAsString);
            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
