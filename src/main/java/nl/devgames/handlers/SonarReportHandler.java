package nl.devgames.handlers;

import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.services.ProjectServiceImpl;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.ProjectService;
import nl.devgames.services.interfaces.UserService;
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
    private static final String ISSUE_IS_NEW = "isNew";
    private static final String ISSUE_SEVERITY = "severity";
    private static final String ISSUE_STATUS = "status";

    public SonarReportHandler() {
    }

    public void processSonarReport(String jsonAsString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonAsString);
        String naam = jsonObject.getJSONArray("user").getJSONObject(0).getString("naam");
        String project = jsonObject.getJSONArray("user").getJSONObject(0).getString("project");
        naam = naam.replaceAll("\\s+","_");
        JSONObject report = jsonObject.getJSONObject("report");
        JSONArray issues = report.getJSONArray("issues");
        long points = issuesToPoints(issues);
        User user = createOrUpdateUser(naam,points);
        createOrUpdateProject(project,user);

    }
    private Long issuesToPoints(JSONArray issues) throws JSONException {
        long points = 0;
        for (int issueIndex = 0; issueIndex < issues.length(); issueIndex++) {
            JSONObject issue = issues.getJSONObject(issueIndex);
            String severity = issue.getString(ISSUE_SEVERITY);
            long tempPoints = 0;
            if(severity.equals(ISSUE_LEVEL_MINOR)){
                tempPoints += 1;
            }else if(severity.equals(ISSUE_LEVEL_MAJOR)){
                tempPoints += 5;
            }else if(severity.equals(ISSUE_LEVEL_CRITICAL)){
                tempPoints += 10;
            }
            if(issue.getString(ISSUE_STATUS).equals(ISSUE_STATUS_CLOSED)){
                points += tempPoints;

            }else{
                if(Boolean.parseBoolean(issue.getString(ISSUE_IS_NEW))){
                    points -= tempPoints;
                }
            }
        }
        return points;
    }
    private User createOrUpdateUser(String naam,Long points){
        UserService userService = new UserServiceImpl();
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
        userService.save(user);
        return user;
    }
    private Project createOrUpdateProject(String projectName,User user){
        ProjectService projectService = new ProjectServiceImpl();
        Project project = projectService.findProjectByProjectName(projectName);
        if(project == null){
            project = new Project();
            project.addProjectMember(user);
            project.setName(projectName);
        }else{
            project.addProjectMember(user);
        }
        projectService.save(project);
        return project;
    }
}
