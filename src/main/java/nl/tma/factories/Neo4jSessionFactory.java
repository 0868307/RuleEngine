package nl.tma.factories;

import org.neo4j.ogm.service.Components;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

        private final static SessionFactory sessionFactory = new SessionFactory("nl.tma.entities");
        private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

        public static Neo4jSessionFactory getInstance() {
                return factory;
        }

        private Neo4jSessionFactory() {
                Components.configuration()
                        .driverConfiguration()
                        .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
//                        .setURI("file:///etc/ruleengine/database");
                        .setURI("file:///C:/database");
        }

        public Session getNeo4jSession() {
                return sessionFactory.openSession();
        }
}