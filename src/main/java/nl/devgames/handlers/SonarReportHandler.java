package nl.devgames.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.devgames.entities.Issue;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.services.ProjectServiceImpl;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.ProjectService;
import nl.devgames.services.interfaces.UserService;
import org.neo4j.ogm.json.JSONArray;
import org.neo4j.ogm.json.JSONException;
import org.neo4j.ogm.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wouter on 5/26/2016.
 */
public class SonarReportHandler {
    private static final String ISSUE_LEVEL_MINOR = "MINOR";
    private static final String ISSUE_LEVEL_MAJOR = "MAJOR";
    private static final String ISSUE_LEVEL_CRITICAL = "CRITICAL";
    private static final String ISSUE_STATUS_OPEN = "OPEN";
    private static final String ISSUE_STATUS_CLOSED = "CLOSED";

    public SonarReportHandler() {
    }

    public void processSonarReport(String jsonAsString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonAsString);
        String name = jsonObject.getJSONArray("user").getJSONObject(0).getString("naam");
        String projectName = jsonObject.getJSONArray("user").getJSONObject(0).getString("project");
        name = name.replaceAll("\\s+","_");
        JSONObject report = jsonObject.getJSONObject("report");
        JSONArray jsonIssues = report.getJSONArray("issues");
        List<Issue> issues = convertJsonToIssues(jsonIssues);
        User user = createOrUpdateUser(name);
        Project project = createOrUpdateProject(projectName,user);
        List<Issue> currentIssues = compareIssues(project.getIssues(),issues);
        project.setIssues(currentIssues);
        issuesToPoints(currentIssues);
        ProjectService projectService = new ProjectServiceImpl();
        projectService.save(project);
    }
    private List<Issue> convertJsonToIssues(JSONArray jsonIssues) throws JSONException {
        List<Issue> issues = new ArrayList<>();
        for (int issueIndex = 0; issueIndex < jsonIssues.length(); issueIndex++) {
            Issue issue;
            try {
                issue = new ObjectMapper().readValue(jsonIssues.getString(issueIndex),Issue.class);
                issues.add(issue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return issues;
    }
    // returns all new and resolved issues
    private List<Issue> compareIssues(List<Issue> oldIssues,List<Issue> newIssues){
        List<Issue> issues = new ArrayList<>();
        for(Issue oldIssue : oldIssues){
            // the old resolved issues aren't relevant anymore for the calculations
            if(oldIssue.getStatus().equals(ISSUE_STATUS_CLOSED)){
                oldIssues.remove(oldIssue);
                continue;
            }
            for(Issue newIssue : newIssues){
                if(oldIssue.getComponent().equals(newIssue.getComponent()) &&
                        oldIssue.getSeverity().equals(newIssue.getSeverity()) &&
                        oldIssue.getRule().equals(newIssue.getRule()) &&
                        oldIssue.getMessage().equals(newIssue.getMessage())){
                    newIssue.setNew(false);
                    issues.add(newIssue);
                    newIssues.remove(newIssue);
                    oldIssues.remove(oldIssue);
                    break;
                }
            }
        }
        // before we removed all the issues that were in the old version from newIssues so the ones left are the new issues
        for(Issue newIssue : newIssues){
            issues.add(newIssue);
        }
        // before we removed all the issues that are still in the new version from oldIssues so all that is left is the resolved issues
        for(Issue oldIssue: oldIssues){
            oldIssue.setStatus(ISSUE_STATUS_CLOSED);
            issues.add(oldIssue);
        }
        return issues;
    }

    /**
     * points based on the severity and amount of same mistakes
     * @param issues
     * @return
     * @throws JSONException
     */
    private Long issuesToPoints(List<Issue> issues) throws JSONException {
        long points = 0;
        Map<String,Integer> ruleMultiplier = new HashMap<String,Integer>();
        for(Issue issue : issues){
            int currentCount = ruleMultiplier.get(issue.getRule());
            ruleMultiplier.replace(issue.getRule(),++currentCount);
        }
        for (Issue issue : issues) {
            String severity = issue.getSeverity();
            long tempPoints = 0;
            if(severity.equals(ISSUE_LEVEL_MINOR)){
                tempPoints += 1;
            }else if(severity.equals(ISSUE_LEVEL_MAJOR)){
                tempPoints += 5;
            }else if(severity.equals(ISSUE_LEVEL_CRITICAL)){
                tempPoints += 10;
            }
            if(issue.getStatus().equals(ISSUE_STATUS_CLOSED)){
                points += tempPoints * (1 + ruleMultiplier.get(issue.getRule()) / 10);
            }else{
                if(issue.isNew()){
                    points -= tempPoints * (1 + ruleMultiplier.get(issue.getRule()) / 10);
                }
            }
        }
        return points;
    }
    private User createOrUpdateUser(String naam){
        UserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(naam);
        if(user == null){
            user = new User();
            user.setGithubUsername(naam);
            user.setPassword("nieuw");
            user.setUsername(naam);
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
