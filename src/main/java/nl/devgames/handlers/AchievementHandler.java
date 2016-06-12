package nl.devgames.handlers;

import nl.devgames.entities.Achievement;
import nl.devgames.entities.User;
import nl.devgames.services.UserServiceImpl;
import nl.devgames.services.interfaces.UserService;

import java.util.Set;

/**
 * Created by Wouter on 6/10/2016.
 */
public class AchievementHandler {
    private static final String TITLE_LINES_ACHIEVEMENT = "Murder she wrote";
    private static final String TITLE_COMMENT_ACHIEVEMENT = "Doc is my name";
    private static final String TITLE_DUPLICATION_ACHIEVEMENT = "I like duplications";
    private static final String REASON_LINES_ACHIEVEMENT = "you wrote more than 1000 lines of code";
    private static final String REASON_COMMENT_ACHIEVEMENT = "you wrote more than 1000 lines of comments";
    private static final String REASON_DUPLICATION_ACHIEVEMENT = "you have more than 5 duplications in your code";
    public AchievementHandler() {
    }
    public void handleAchievements(User user){
        boolean newAchievements = false;
        newAchievements = newAchievements || checkLines(user);
        newAchievements = newAchievements || checkComments(user);
        newAchievements = newAchievements || checkDuplications(user);
        if(newAchievements){
            //TODO verstuur bericht naar telefoon
        }
    }

    private boolean checkDuplications(User user) {
        long duplicationBlocksWritten = user.getDuplicationBlocksWritten();
        long duplicationFilesWritten = user.getDuplicationFilesWritten();
        long duplicationLinesWritten = user.getDuplicationLinesWritten();
        long duplications = duplicationBlocksWritten + duplicationFilesWritten + duplicationLinesWritten;
        if(duplications > 5){
            Achievement achievement = new Achievement();
            achievement.setTitle(TITLE_DUPLICATION_ACHIEVEMENT);
            achievement.setReason(REASON_DUPLICATION_ACHIEVEMENT);
            user.addAchievements(achievement);
            saveUser(user);
            return true;
        }
        return false;
    }

    private boolean checkLines(User user){
        long linesWritten = user.getLinesWritten();

        if(linesWritten > 1000){
            Achievement achievement = new Achievement();
            achievement.setTitle(TITLE_LINES_ACHIEVEMENT);
            achievement.setReason(REASON_LINES_ACHIEVEMENT);
            user.addAchievements(achievement);
            saveUser(user);
            return true;
        }
        return false;
    }
    private boolean checkComments(User user){
        long linesCommented = user.getLinesCommented();
        Achievement achievement = null;
        if(linesCommented > 1000){
            achievement = new Achievement();
            achievement.setTitle(TITLE_COMMENT_ACHIEVEMENT);
            achievement.setReason(REASON_COMMENT_ACHIEVEMENT);
            user.addAchievements(achievement);
            saveUser(user);
            return true;
        }
        return false;
    }
    private void saveUser(User user){
        UserService userService = new UserServiceImpl();

    }
}
