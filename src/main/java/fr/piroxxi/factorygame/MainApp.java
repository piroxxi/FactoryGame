package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.Game;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(MainAppConfig.class);
        context.refresh();

        Game game = context.getBean(Game.class);
        game.start();
    }
}
