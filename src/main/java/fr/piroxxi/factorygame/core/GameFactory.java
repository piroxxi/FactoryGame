package fr.piroxxi.factorygame.core;

import fr.piroxxi.factorygame.core.model.Game;
import fr.piroxxi.factorygame.log.Log;

import org.slf4j.Logger;


public class GameFactory {
    @Log
    private static Logger LOG;

    public Game createGame(int size) {
        Game game = new Game(size);
//        game.setEventsHandler();
        return game;
    }
}
