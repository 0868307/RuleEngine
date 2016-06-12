package nl.devgames.services.interfaces;

import nl.devgames.entities.Commit;
import nl.devgames.entities.PastCommits;
import nl.devgames.entities.User;

/**
 * Created by draikos on 6/12/2016.
 */
public interface PastCommit {
    void save(PastCommits pastCommits);
    PastCommits findPastCommitByUsername(String username);
}
