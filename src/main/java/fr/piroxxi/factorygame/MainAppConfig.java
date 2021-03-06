package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.GameFactory;
import fr.piroxxi.factorygame.event.ActionEventBus;
import fr.piroxxi.factorygame.storage.hibernate.HibernateStorage;
import fr.piroxxi.factorygame.storage.Storage;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "fr.piroxxi.factorygame.log") // Loads LogInjector
public class MainAppConfig {
    private static Logger LOG = LoggerFactory.getLogger(MainAppConfig.class);
    private static final String UTF8 = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Storage storage() {
        return new HibernateStorage(getSessionFactory());
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ActionEventBus actionEventBus() {
        return new ActionEventBus();
    }

    @Bean
    public GameFactory gameFactory() {
        return new GameFactory();
    }

    @Bean
    public SessionFactory getSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
