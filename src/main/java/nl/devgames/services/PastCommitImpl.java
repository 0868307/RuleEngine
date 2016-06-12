package nl.devgames.services;

import nl.devgames.entities.PastCommits;
import nl.devgames.entities.User;
import nl.devgames.factories.Neo4jSessionFactory;
import nl.devgames.services.interfaces.PastCommit;
import org.neo4j.ogm.cypher.Filter;

import java.util.Iterator;

/**
 * Created by draikos on 6/12/2016.
 */
public class PastCommitImpl extends GenericService<PastCommits> implements PastCommit{
    @Override
    public Class<PastCommits> getEntityType() {
        return PastCommits.class;
    }
    @Override
    public void save(PastCommits pastCommits) {
        createOrUpdate(pastCommits);

    }

    @Override
    public PastCommits findPastCommitByUsername(String username) {
        PastCommits pastCommits = null;
        Iterator iterator = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(PastCommits.class, new Filter("username", username)).iterator();
        while(iterator.hasNext()){
            pastCommits = (PastCommits) iterator.next();
            break;
        }
        return pastCommits;
    }

}
