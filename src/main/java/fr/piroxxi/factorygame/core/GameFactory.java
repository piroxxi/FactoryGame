package fr.piroxxi.factorygame.core;

import fr.piroxxi.factorygame.core.model.Game;
import fr.piroxxi.factorygame.event.ActionEventBus;
import fr.piroxxi.factorygame.log.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class GameFactory {
    @Log
    private static Logger LOG;

    @Autowired
    private ApplicationContext applicationContext;

    public Game createGame(int size) {
        Game game = new Game(size);
        game.setActionEventBus(this.applicationContext.getBean(ActionEventBus.class));
        return game;
    }
}
