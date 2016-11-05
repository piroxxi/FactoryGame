package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MainApp implements WebApplicationInitializer {
    private static Logger LOG = LoggerFactory.getLogger(MainApp.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context =
                new AnnotationConfigWebApplicationContext();
        context.register(MainAppConfig.class);
        context.setServletContext(servletContext);
        context.refresh();

        // Spring MVC front controller
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);

        LOG.info("\n---------------------------------------\n>> STARTING GAME :\n\n\n");
        Game game = context.getBean(Game.class);
        game.start();
    }
}
