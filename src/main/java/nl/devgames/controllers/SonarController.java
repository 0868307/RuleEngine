package nl.devgames.controllers;

import com.cedarsoftware.util.io.JsonWriter;
import nl.devgames.Application;
import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.services.CommitServiceImpl;
import nl.devgames.services.UserServiceImpl;
import org.neo4j.ogm.json.JSONException;
import org.neo4j.ogm.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Created by Wouter on 3/5/2016.
 */
@RestController
public class SonarController {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @RequestMapping(value = "/sonar",method = RequestMethod.POST)
    public Map saveSonarInfo(JSONObject json) {
        try {
            logger.info("sonar_output", json.toString(4));
        } catch (JSONException e) {
            logger.warn("sonar_output","No JsonObject");
        }
        return json.toMap();
    }
}
