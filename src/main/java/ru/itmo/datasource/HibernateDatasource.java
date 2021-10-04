package ru.itmo.datasource;

import ru.itmo.ConfigReader;
import  ru.itmo.entities.DBCoordinates;
import  ru.itmo.entities.DBLocation;
import  ru.itmo.entities.DBPerson;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


import java.util.Properties;


// TODO независимость от окружения
// TODO фильтрация по полям
// TODO пагинация

public class HibernateDatasource {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                ConfigReader conf = new ConfigReader();

                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, conf.getDb_url());
                settings.put(Environment.USER, conf.getUser());
                settings.put(Environment.PASS, conf.getPass());
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.SHOW_SQL, "true"); // will write sql statements in log

                settings.put(Environment.HBM2DDL_AUTO, "create"); // on start rewrite db with our schema

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(DBPerson.class);
                configuration.addAnnotatedClass(DBLocation.class);
                configuration.addAnnotatedClass(DBCoordinates.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                System.out.println("Hibernate Java Config serviceRegistry created");
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
