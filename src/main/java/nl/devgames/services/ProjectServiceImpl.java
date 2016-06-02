package nl.devgames.services;

import nl.devgames.entities.Project;
import nl.devgames.entities.User;
import nl.devgames.factories.Neo4jSessionFactory;
import nl.devgames.services.interfaces.ProjectService;
import org.neo4j.ogm.cypher.Filter;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Wouter on 3/5/2016.
 */
public class ProjectServiceImpl extends GenericService<Project> implements ProjectService {
    @Override
    public Class<Project> getEntityType() {
        return Project.class;
    }

    @Override
    public Set<User> getUsersOfProject(String project) {
        return null;
    }

    @Override
    public Project findProjectByProjectName(String projectName) {
        Project project = null;
        Iterator iterator = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(Project.class, new Filter("name", projectName)).iterator();
        while(iterator.hasNext()){
            project = (Project) iterator.next();
            break;
        }
        return project;
    }

    @Override
    public void save(Project project) {
        createOrUpdate(project);
    }
}
