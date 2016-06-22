package nl.tma.handlers;

import nl.tma.entities.Issue;
import nl.tma.entities.User;
import nl.tma.services.UserServiceImpl;

import java.util.*;

/**
 * Created by Wouter on 6/22/2016.
 *
 * Quality Metrics :
 * Design
 * Effective
 * Logic
 * Code Coverage
 *
 */
public class QualityMetricHandler {
    public static final String[] DESIGN_RULES = {
            "squid:S1195",
            "squid:S1197",
            "squid:S00101",
            "squid:S2166",
            "squid:S00115",
            "squid:S00116",
            "squid:S00114",
            "squid:S00117",
            "squid:S1312",
            "squid:LowerCaseLongSuffixCheck",
            "squid:RightCurlyBraceStartLineCheck",
            "squid:S00100",
            "squid:ModifiersOrderCheck",
            "squid:S00120",
            "squid:S1170",
            "squid:S00122",
            "squid:S00105",
            "squid:S1220",
            "squid:S1213",
            "squid:S00119"
    };
    public static final String[] LOGIC_RULES = {
        "squid:S2165",
        "squid:S2442",
        "squid:S2153",
        "squid:S1157",
        "squid:S2094",
        "squid:S2440",
        "squid:S1066",
        "squid:S1155",
        "squid:S1125",
        "squid:S1488",
        "squid:S1185",
        "squid:S1158",
        "squid:S1905",
        "squid:S1126",
        "squid:S1153"
    };
    public static final String[] SECURITY_RULES = {
        "squid:S2254",
        "squid:S1145",
        "squid:S1174",
        "squid:S1444",
        "squid:S2151",
        "squid:S1873",
        "squid:S2092",
        "squid:S2068",
        "squid:S2277",
        "squid:S1313",
        "squid:ObjectFinalizeCheck",
        "squid:S2077"
    };
    public QualityMetricHandler() {
    }

    public void calculateAndSaveMetrics(Map<String,Object> metrics, Set<Issue> issues, long userId) {
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.find(userId);
        user.setDesignFailures(getRulesCount(DESIGN_RULES,issues));
        user.setLogicFailures(getRulesCount(LOGIC_RULES, issues));
        user.setSecurityFailures(getRulesCount(SECURITY_RULES, issues));
        user.setEffectiveLines(getEffectiveLines(metrics));
        user.setCodeCoverage(getUserCodeCoverage(metrics));
        userService.save(user);
    }
    private long getEffectiveLines(Map<String,Object> metrics){
        return (long)metrics.get(SonarReportHandler.convertLinesAndDuplicationsToPoints(metrics));
    }
    private int getRulesCount(String[] rulesAsStringArray,Set<Issue> issues){
        int count = 0;
        Set<String> rules = new HashSet<>(Arrays.asList(rulesAsStringArray));
        for(Issue issue : issues){
            if(rules.contains(issue.getRule())){
                count++;
            }
        }
        return count;
    }
    private double getUserCodeCoverage(Map<String,Object> metrics){
        return 0;
    }
    public Map<String,Double> getQuality(long userId){
        Map<String,Double> qualityMap = new HashMap<>();
        UserServiceImpl userService = new UserServiceImpl();
        User me = userService.find(userId);
        Set<User> users = userService.getAllUsers();
        Double lowDesignFailures = null;
        Double highDesignFailures = null;
        Double lowLogicFailures = null;
        Double highLogicFailures = null;
        Double lowSecurityFailures = null;
        Double highSecurityFailures = null;
        Double lowCodeCoverage = null;
        Double highCodeCoverage = null;
        Double lowEffectiveLines = null;
        Double highEffectiveLines = null;
        for (User user:users){
            if(lowDesignFailures == null || lowDesignFailures > user.getDesignFailures()){
                lowDesignFailures = (double)user.getDesignFailures();
            }
            if(highDesignFailures == null || highDesignFailures < user.getDesignFailures()){
                highDesignFailures = (double)user.getDesignFailures();
            }
            if(lowLogicFailures == null || lowLogicFailures > user.getLogicFailures()){
                lowLogicFailures = (double)user.getLogicFailures();
            }
            if(highLogicFailures == null || highLogicFailures < user.getLogicFailures()){
                highLogicFailures = (double)user.getLogicFailures();
            }
            if(lowSecurityFailures == null || lowSecurityFailures > user.getSecurityFailures()){
                lowSecurityFailures = (double)user.getSecurityFailures();
            }
            if(highSecurityFailures == null || highSecurityFailures < user.getSecurityFailures()){
                highSecurityFailures = (double)user.getSecurityFailures();
            }
            if(lowEffectiveLines == null || lowEffectiveLines > user.getEffectiveLines()){
                lowEffectiveLines = (double)user.getEffectiveLines();
            }
            if(highEffectiveLines == null || highEffectiveLines < user.getEffectiveLines()){
                highEffectiveLines = (double)user.getEffectiveLines();
            }
            if(lowCodeCoverage == null || lowCodeCoverage > user.getCodeCoverage()){
                lowCodeCoverage = user.getCodeCoverage();
            }
            if(highCodeCoverage == null || highCodeCoverage < user.getCodeCoverage()){
                highCodeCoverage = user.getCodeCoverage();
            }
        }
        double designScore = normalize(lowDesignFailures,highDesignFailures,me.getDesignFailures(),1,5);
        double logicScore = normalize(lowLogicFailures,highLogicFailures,me.getLogicFailures(),1,5);
        double securityScore = normalize(lowSecurityFailures,highSecurityFailures,me.getSecurityFailures(),1,5);
        double effectiveLinesScore = normalize(lowEffectiveLines,highEffectiveLines,me.getEffectiveLines(),1,5);
        double codeCoverageScore = normalize(lowCodeCoverage,highCodeCoverage,me.getCodeCoverage(),1,5);
        qualityMap.put("design",designScore);
        qualityMap.put("logic",logicScore);
        qualityMap.put("security",securityScore);
        qualityMap.put("effective",effectiveLinesScore);
        qualityMap.put("codeCoverage",codeCoverageScore);
        return qualityMap;
    }
    private double normalize(double low,double high, double value,double from, double to){
        double scaled = 2*((value-low)/(high-low))-1;
        return ((scaled+1)/2)*(to-from)+from;
    }
}
