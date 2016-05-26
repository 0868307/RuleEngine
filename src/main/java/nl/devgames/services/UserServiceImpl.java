package nl.devgames.services;

import nl.devgames.entities.Commit;
import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.factories.Neo4jSessionFactory;
import nl.devgames.services.interfaces.UserService;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;

import java.util.*;

/**
 * Created by Wouter on 3/5/2016.
 */
public class UserServiceImpl extends GenericService<User> implements UserService {
    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public boolean validateUser(String username, String password) {
        User user = null;
        Iterator iterator = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(User.class, new Filter("username", username)).iterator();
        while(iterator.hasNext()){
            user = (User) iterator.next();
            break;
        }
        if(user != null && user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public Set<Project> findAllProjectsOfUser(Long id) {
        String query = "MATCH (u:" + getEntityType().getSimpleName() + ")<-[r:PROJECT_MEMBER]-(p:Project) WHERE id(u) = "+ id +" RETURN p";
        Iterable<Map<String,Object>> maps = Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, Collections.EMPTY_MAP);
        Set<Project> returnList = new HashSet<>();
        Iterator iterator = maps.iterator();
        while(iterator.hasNext()){
            Map<String,Object> row = (Map<String,Object>)iterator.next();
            for(Map.Entry<String,Object> entry : row.entrySet()){
                returnList.add((Project)entry.getValue());
            }
        }
        return returnList;
    }

    @Override
    public Set<Commit> findAllCommitsOfUser(User user) {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = null;
        Iterator iterator = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(User.class, new Filter("username", username)).iterator();
        while(iterator.hasNext()){
            user = (User) iterator.next();
            break;
        }
        return user;
    }

    @Override
    public User findUserByGithubname(String githubname) {
        User user = null;
        Iterator iterator = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(User.class, new Filter("githubUsername", githubname)).iterator();
        while(iterator.hasNext()){
            user = (User) iterator.next();
            break;
        }
        return user;
    }

    @Override
    public Set<User> getAllUsers() {
        Iterator iterator = findAll().iterator();
        Set<User> users = new HashSet<>();
        while(iterator.hasNext()){
            User user = (User) iterator.next();
            users.add(user);
        }
        return users;
    }
}
