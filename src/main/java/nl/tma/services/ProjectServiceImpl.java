package nl.tma.services;

import nl.tma.entities.Project;
import nl.tma.factories.Neo4jSessionFactory;
import nl.tma.services.interfaces.ProjectService;
import org.neo4j.ogm.cypher.Filter;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Wouter on 3/5/2016.
 */
public class ProjectServiceImpl extends GenericService<Project> implements ProjectService {
    @Override
    public Class<Project> getEntityType() {
        return Project.class;
    }

    @Override
    public Project findProjectByProjectName(String projectName) {
        Project project = null;
        Collection<Project> projectCollection = Neo4jSessionFactory.getInstance().getNeo4jSession().loadAll(Project.class, new Filter("name", projectName));
        if(projectCollection != null){
            Iterator iterator = projectCollection.iterator();
            while(iterator.hasNext()){
                project = (Project) iterator.next();
                break;
            }
        }
        return project;
    }

    @Override
    public void save(Project project) {
        createOrUpdate(project);
    }
}
