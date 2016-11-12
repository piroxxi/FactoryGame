package fr.piroxxi.factorygame.core.model;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {
    @Test
    public void testGenerateMap() {
        Game game = new Game(6);
        game.generateMap();
        Assert.assertNotNull(game.getTile(2, 3));
        Assert.assertNull(game.getTile(6, 3));
    }
}
