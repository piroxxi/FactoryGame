package fr.piroxxi.factorygame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This is where all the magic belongs
 */
public class MainApp implements WebApplicationInitializer {
    /*
     * If you are starting reading the code from this class (which you should, as it's the main
     * entry point for the application), here are a couple facts :
     * - This class creates the "AnnotationConfigWebApplicationContext" which is the "Spring context"
     * responsible for handling Spring's beans. All beans will have their dependencies injected
     * (mechanisme which is configured in the MainAppConfig class)
     * - The LOG object is created statically as instances of this class are not "injected" with
     * a LOG implementation. (this is also the case with Game)
     *
     * The "Game" class (and most classes in the core) are more that just POJOs, they have a bit of logic
     * in them. This avoid
     *
     */
    private static Logger LOG = LoggerFactory.getLogger(MainApp.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context =
                new AnnotationConfigWebApplicationContext();
        context.register(MainAppConfig.class);
        context.register(MainAppWebConfig.class);
        context.setServletContext(servletContext);
        context.refresh();

        // Spring MVC front controller
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        servlet.addMapping("/");
        servlet.addMapping("/css");
        servlet.addMapping("/js");
        servlet.setLoadOnStartup(1);
    }
}
