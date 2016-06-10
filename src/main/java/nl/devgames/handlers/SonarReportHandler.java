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
import java.util.*;

/**
 * Created by Wouter on 5/26/2016.
 */
public class SonarReportHandler {
    private static final String ISSUE_LEVEL_MINOR = "MINOR";
    private static final String ISSUE_LEVEL_MAJOR = "MAJOR";
    private static final String ISSUE_LEVEL_CRITICAL = "CRITICAL";
    private static final String ISSUE_STATUS_OPEN = "OPEN";
    private static final String ISSUE_STATUS_CLOSED = "CLOSED";
    private static final String METRICS_KEY_LINES_COMMENTED = "comment_lines";
    private static final String METRICS_KEY_LINES = "ncloc";
    private static final String METRICS_KEY_DUPLICATION_LINES = "duplicated_lines";
    private static final String METRICS_KEY_DUPLICATION_LINES_DENSITY = "duplicated_lines_density";
    private static final String METRICS_KEY_DUPLICATION_BLOCKS = "duplicated_blocks";
    private static final String METRICS_KEY_DUPLICATION_FILES = "duplicated_files";
    private static final String METRICS_KEY_DEBT = "sqale_index";


    public SonarReportHandler() {
    }

    public void processSonarReport(String jsonAsString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonAsString);
        String name = jsonObject.getJSONArray("user").getJSONObject(0).getString("naam");
        String projectName = jsonObject.getJSONArray("user").getJSONObject(0).getString("project");
        name = name.replaceAll("\\s+","_");
        JSONObject report = jsonObject.getJSONObject("report");
        JSONArray metrics = getMetricsFromReport(report);
        JSONArray jsonIssues = report.getJSONArray("issues");
        Set<Issue> issues = convertJsonToIssues(jsonIssues);
        User user = createOrUpdateUser(name);
        Project project = createOrUpdateProject(projectName,user);
        Set<Issue> currentIssues = compareIssues(project.getIssues(),issues);
        project.setIssues(currentIssues);
        calculatePointsAndUpdateUser(metrics, currentIssues, project, user);
        ProjectService projectService = new ProjectServiceImpl();
        projectService.save(project);
    }

    private Long calculatePointsAndUpdateUser(JSONArray metrics,Set<Issue> issues,Project project,User user) throws JSONException {
        metricsToPointsAndSaveToUser(metrics,project,user);
        issuesToPoints(issues);
        return null;
    }

    private Long metricsToPointsAndSaveToUser(JSONArray metrics,Project project,User user) throws JSONException {
        long points = 0;
        for (int metricIndex = 0; metricIndex < metrics.length(); metricIndex++) {
            JSONObject current = metrics.getJSONObject(metricIndex);
            String value = current.getString("val");
            switch (current.getString("key")){
                case METRICS_KEY_LINES:
                    user.setLinesWritten(user.getLinesWritten() + (project.getLines() - Long.parseLong(value)));
                    break;
                case METRICS_KEY_LINES_COMMENTED:
                    user.setLinesCommented(user.getLinesCommented() + (project.getLinesCommented() - Long.parseLong(value)));
                    break;
                case METRICS_KEY_DUPLICATION_LINES:
                    user.setDuplicationLinesWritten(user.getDuplicationLinesWritten() + (project.getDuplicationLines() - Long.parseLong(value)));
                    break;
                case METRICS_KEY_DUPLICATION_LINES_DENSITY:
                    Double.parseDouble(value);
                    break;
                case METRICS_KEY_DUPLICATION_BLOCKS:
                    user.setDuplicationBlocksWritten(user.getDuplicationBlocksWritten() + (project.getDuplicationBlocks() - Long.parseLong(value)));
                    break;
                case METRICS_KEY_DUPLICATION_FILES:
                    user.setDuplicationFilesWritten(user.getDuplicationFilesWritten() + (project.getDuplicationFiles() - Long.parseLong(value)));
                    break;
                case METRICS_KEY_DEBT:
                    user.setDebtCreated(user.getDebtCreated() + (project.getDebt() - Double.parseDouble(value)));
                    break;
            }
        }
        return points;
    }

    private Set<Issue> convertJsonToIssues(JSONArray jsonIssues) throws JSONException {
        Set<Issue> issues = new HashSet<>();
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
    private JSONArray getMetricsFromReport(JSONObject report) throws JSONException {
        Map<String,Double> metrics = new HashMap<>();
        JSONArray parent = report.getJSONArray("metrics");
        JSONObject sonarProject = parent.getJSONObject(0);
        JSONArray metricsAsArray = sonarProject.getJSONArray("msr");
        return metricsAsArray;
    }
    // returns all new and resolved issues
    private Set<Issue> compareIssues(Set<Issue> oldIssues,Set<Issue> newIssues){
        Set<Issue> issues = new HashSet<>();
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
    private Long issuesToPoints(Set<Issue> issues) throws JSONException {
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
