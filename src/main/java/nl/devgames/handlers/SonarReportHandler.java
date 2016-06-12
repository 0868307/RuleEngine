package nl.devgames.handlers;

import com.google.gson.Gson;
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
        JSONArray metrics = getMetricsFromReport(jsonObject);
        JSONArray jsonIssues = report.getJSONArray("issues");
        Set<Issue> issues = convertJsonToIssues(jsonIssues);
        User user = createOrUpdateUser(name);
        Project project = createOrUpdateProject(projectName,user);
        Set<Issue> currentIssues = compareIssues(project.getIssues(),issues);
        metricsToPointsAndSaveToUserAndProject(metrics, currentIssues, project, user);
    }

    private Long metricsToPointsAndSaveToUserAndProject(JSONArray metrics,Set<Issue> issues,Project project,User user) throws JSONException {
        UserService userService = new UserServiceImpl();
        ProjectService projectService = new ProjectServiceImpl();
        long points = 0;
        Map<String,Object> metricsAsMap = new HashMap<>();
        for (int metricIndex = 0; metricIndex < metrics.length(); metricIndex++) {
            JSONObject current = metrics.getJSONObject(metricIndex);
            String value = current.getString("val");
            String key = current.getString("key");
            if(value.contains(".")){
                value = (long)Double.parseDouble(value)+"";
            }
            switch (key){
                case METRICS_KEY_LINES:
                    long lines = Long.parseLong(value) - project.getLines();
                    project.setLines(Long.parseLong(value));
                    metricsAsMap.put(key,lines);
                    user.setLinesWritten(user.getLinesWritten() + lines);
                    break;
                case METRICS_KEY_LINES_COMMENTED:
                    lines = Long.parseLong(value) -project.getLinesCommented();
                    project.setLinesCommented(Long.parseLong(value));
                    metricsAsMap.put(key,lines);
                    user.setLinesCommented(user.getLinesCommented() + lines);
                    break;
                case METRICS_KEY_DUPLICATION_LINES:
                    long duplications = Long.parseLong(value) - project.getDuplicationLines();
                    project.setDuplicationLines(Long.parseLong(value));
                    metricsAsMap.put(key,duplications);
                    user.setDuplicationLinesWritten(user.getDuplicationLinesWritten() + duplications);
                    break;
                case METRICS_KEY_DUPLICATION_BLOCKS:
                    duplications = Long.parseLong(value) - project.getDuplicationBlocks();
                    project.setDuplicationBlocks(Long.parseLong(value));
                    metricsAsMap.put(key,duplications);
                    user.setDuplicationBlocksWritten(user.getDuplicationBlocksWritten() + duplications);
                    break;
                case METRICS_KEY_DUPLICATION_FILES:
                    duplications = Long.parseLong(value) - project.getDuplicationFiles();
                    project.setDuplicationFiles(Long.parseLong(value));
                    metricsAsMap.put(key,duplications);
                    user.setDuplicationFilesWritten(user.getDuplicationFilesWritten() + duplications);
                    break;
                case METRICS_KEY_DEBT:
                    double debtCreated = Double.parseDouble(value) - project.getDebt();
                    project.setDebt(Double.parseDouble(value));
                    metricsAsMap.put(key,debtCreated);
                    user.setDebtCreated(user.getDebtCreated() + debtCreated);
                    break;
                default:
                    break;
            }
        }
        project.setIssues(issues);
        points += metricsToPoints(metricsAsMap,issues);
        user.setPoints(user.getPoints() + points);
        projectService.save(project);
        userService.save(user);
        return points;
    }

    private long metricsToPoints(Map<String, Object> metricsAsMap, Set<Issue> issues) throws JSONException {
        long points = 0;
        points += convertLinesAndDuplicationsToPoints(metricsAsMap);
        double debt = 0;
        if(metricsAsMap.containsKey(METRICS_KEY_DEBT)){
            debt = (double)metricsAsMap.get(METRICS_KEY_DEBT);
        }
        System.out.println("POINTS Lines: " +points);
        points += convertIssuesToPoints(issues, debt);
        System.out.println("POINTS Issues +LINES : " +points);
        return points;
    }

    // calculate the "effective" lines added and use it as points
    private long convertLinesAndDuplicationsToPoints(Map<String, Object> metricsAsMap) {
        long points = 0;
        if(metricsAsMap.containsKey(METRICS_KEY_LINES)){
            long lines = (Long)metricsAsMap.get(METRICS_KEY_LINES);
            long duplications = 0;
            if(metricsAsMap.containsKey(METRICS_KEY_DUPLICATION_BLOCKS)){
                duplications += (Long)metricsAsMap.get(METRICS_KEY_DUPLICATION_BLOCKS);
            }
            if(metricsAsMap.containsKey(METRICS_KEY_DUPLICATION_LINES)){
                duplications += (Long)metricsAsMap.get(METRICS_KEY_DUPLICATION_LINES);
            }
            if(metricsAsMap.containsKey(METRICS_KEY_DUPLICATION_FILES)){
                duplications += (Long)metricsAsMap.get(METRICS_KEY_DUPLICATION_FILES);
            }
            if(duplications > 0) {
                points += lines/duplications;
            }else{
                points +=lines;
            }
        }
        return points;
    }

    private Set<Issue> convertJsonToIssues(JSONArray jsonIssues) throws JSONException {
        Set<Issue> issues = new HashSet<>();
        for (int issueIndex = 0; issueIndex < jsonIssues.length(); issueIndex++) {
            Issue issue;
            Gson gson = new Gson();
            issue = gson.fromJson(jsonIssues.getString(issueIndex), Issue.class);
            issues.add(issue);
        }
        return issues;
    }
    private JSONArray getMetricsFromReport(JSONObject jsonInput) throws JSONException {
        JSONArray parent = jsonInput.getJSONArray("metrics");
        JSONObject sonarProject = parent.getJSONObject(0);
        JSONArray metricsAsArray = sonarProject.getJSONArray("msr");
        return metricsAsArray;
    }
    // returns all new and resolved issues
    private Set<Issue> compareIssues(Set<Issue> oldIssues,Set<Issue> newIssues){
        Set<Issue> issues = new HashSet<>();
        Set<Issue> copyNewIssues = new HashSet<>();
        copyNewIssues.addAll(newIssues);
        if(oldIssues != null){

            Set<Issue> copyOldIssues = new HashSet<>();
            copyOldIssues.addAll(oldIssues);
            for(Issue oldIssue : oldIssues){
                // the old resolved issues aren't relevant anymore for the calculations
                if(oldIssue.getStatus().equals(ISSUE_STATUS_CLOSED)){
                    copyOldIssues.remove(oldIssue);
                    continue;
                }
                for(Issue newIssue : newIssues){
                    if(oldIssue.getComponent().equals(newIssue.getComponent()) &&
                            oldIssue.getSeverity().equals(newIssue.getSeverity()) &&
                            oldIssue.getRule().equals(newIssue.getRule()) &&
                            oldIssue.getMessage().equals(newIssue.getMessage())){
                        newIssue.setNew(false);
                        issues.add(newIssue);
                        copyNewIssues.remove(newIssue);
                        copyOldIssues.remove(oldIssue);
                        break;
                    }
                }
            }
            // before we removed all the issues that are still in the new version from oldIssues so all that is left is the resolved issues
            for(Issue oldIssue: copyOldIssues){
                oldIssue.setStatus(ISSUE_STATUS_CLOSED);
                issues.add(oldIssue);
            }
        }
        // before we removed all the issues that were in the old version from newIssues so the ones left are the new issues
        for(Issue newIssue : copyNewIssues){
            issues.add(newIssue);
        }
        return issues;
    }

    /**
     * points based on the severity and amount of same mistakes and the debt created this commit
     * @param issues
     * @return
     * @throws JSONException
     */
    private Long convertIssuesToPoints(Set<Issue> issues,double debt) throws JSONException {
        long points = 0;
        Map<String,Integer> ruleMultiplierMap = new HashMap<String,Integer>();
        for(Issue issue : issues){
            int currentCount = 0;
            if(ruleMultiplierMap.containsKey(issue.getRule())){
                currentCount = ruleMultiplierMap.get(issue.getRule());
            }
            ruleMultiplierMap.replace(issue.getRule(), ++currentCount);
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
            long ruleMultiplier = 1;
            if(ruleMultiplierMap.containsKey(issue.getRule())){
                ruleMultiplier = (1 + ruleMultiplierMap.get(issue.getRule()) / 10);
            }
            long debtMultiplier = (long)(1+ debt/10);
            tempPoints = tempPoints * ruleMultiplier * debtMultiplier;
            if(issue.getStatus().equals(ISSUE_STATUS_CLOSED)){
                points += tempPoints;
            }else{
                if(issue.isNew()){
                    points -= tempPoints;
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
