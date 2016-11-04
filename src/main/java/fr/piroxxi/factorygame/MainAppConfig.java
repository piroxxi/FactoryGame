package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.Game;
import fr.piroxxi.factorygame.storage.hibernate.HibernateStorage;
import fr.piroxxi.factorygame.storage.Storage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "fr.piroxxi.factorygame")
public class MainAppConfig {
    @Bean
    public Storage storage() {
        return new HibernateStorage(getSessionFactory());
    }

    @Bean
    public SessionFactory getSessionFactory() {
//        try {
//            org.hibernate.cfg.Configuration configuration;
//            configuration = new org.hibernate.cfg.Configuration().configure("/hibernate.cfg.xml");
//            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
//            serviceRegistryBuilder.applySettings(configuration.getProperties());
//            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
//            return configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Bean
    public Game game() {
        return new Game(storage());
    }
}
