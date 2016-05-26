package nl.devgames.handlers;

import nl.devgames.entities.User;
import nl.devgames.services.UserServiceImpl;
import org.neo4j.ogm.json.JSONArray;
import org.neo4j.ogm.json.JSONException;
import org.neo4j.ogm.json.JSONObject;

/**
 * Created by Wouter on 5/26/2016.
 */
public class SonarReportHandler {
    private static final String ISSUE_LEVEL_MINOR = "MINOR";
    private static final String ISSUE_LEVEL_MAJOR = "MAJOR";
    private static final String ISSUE_LEVEL_CRITICAL = "CRITICAL";
    private static final String ISSUE_STATUS_OPEN = "OPEN";
    private static final String ISSUE_STATUS_CLOSED = "CLOSED";

    public static void processSonarReport(String jsonAsString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonAsString);
        String naam = jsonObject.getJSONArray("user").getJSONObject(0).getString("naam");
        naam = naam.replaceAll("\\s+","_");
        JSONObject report = jsonObject.getJSONObject("report");
        JSONArray issues = report.getJSONArray("issues");
        long points = issuesToPoints(issues);
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.findUserByUsername(naam);
        if(user == null){
            user = new User();
            user.setGithubUsername(naam);
            user.setPassword("nieuw");
            user.setUsername(naam);
            user.setPoints(points);
        }else{
            user.setPoints(user.getPoints() + points);
        }
        userService.createOrUpdate(user);
    }
    private static Long issuesToPoints(JSONArray issues) throws JSONException {
        long points = 0;
        for (int issueIndex = 0; issueIndex < issues.length(); issueIndex++) {
            JSONObject issue = issues.getJSONObject(issueIndex);
            if(issue.getString("status").equals(ISSUE_STATUS_CLOSED)){
                String severity = issue.getString("severity");
                if(severity.equals(ISSUE_LEVEL_MINOR)){
                    points += 1;
                }else if(severity.equals(ISSUE_LEVEL_MAJOR)){
                    points += 5;
                }else if(severity.equals(ISSUE_LEVEL_CRITICAL)){
                    points += 10;
                }
            }
        }
        return points;
    }
}
