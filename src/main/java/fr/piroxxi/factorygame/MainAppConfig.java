package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.Game;
import fr.piroxxi.factorygame.storage.hibernate.HibernateStorage;
import fr.piroxxi.factorygame.storage.Storage;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "fr.piroxxi.factorygame")
public class MainAppConfig extends WebMvcConfigurerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(MainAppConfig.class);
    private static final String UTF8 = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Storage storage() {
        return new HibernateStorage(getSessionFactory());
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

    @Bean
    public Game game() {
        return new Game(storage());
    }


    /*
     * ********************************************************************************************************
     * * https://github.com/jmiguelsamper/thymeleaf3-spring-helloworld/blob/master/src/main/java/com/thymeleafexamples/thymeleaf3/config/ThymeleafConfig.java
     * ********************************************************************************************************
     */

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(UTF8);
        return resolver;
    }

    private TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }


    /*
     * ******** FOR STATIC FILES ********
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/statics/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/statics/js/");
    }
}
