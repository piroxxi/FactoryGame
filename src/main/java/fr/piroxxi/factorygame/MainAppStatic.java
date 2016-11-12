package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.exceptions.IllegalGameAction;
import fr.piroxxi.factorygame.core.model.*;
import fr.piroxxi.factorygame.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.piroxxi.factorygame.core.GameFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainAppStatic {
    private static Logger LOG = LoggerFactory.getLogger(Game.class);

    public static void main(String args[]) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(MainAppConfig.class);
        context.refresh();

        LOG.info("\n\n\n####################################################################");

        GameFactory factory = context.getBean(GameFactory.class);
        Game game = factory.createGame(6);
        ActionEventBus gameActionEventBus = game.getActionEventBus();
        ArrayList<ActionEvent> turnEvents = new ArrayList<>();
        gameActionEventBus.on(ActionEvent.class, new ActionEventHandler<ActionEvent>() {
            @Override
            public void event(ActionEvent event) {
                turnEvents.add(event);
            }
        });


        game.generateMap();
        Player p1 = new Player(game, "Raphael");
        p1.setMoney(8000);
        game.addPlayer(p1);
        p1 = new Player(game, "Chloe");
        p1.setMoney(8000);
        game.addPlayer(p1);
        p1 = new Player(game, "Andrew");
        p1.setMoney(8000);
        game.addPlayer(p1);
        p1 = new Player(game, "Jules");
        p1.setMoney(8000);
        game.addPlayer(p1);
        game.info();

        int turn = 0;
        while (game.getFreeTiles().size() > 0) {
            LOG.info("START OF TURN " + turn + " :");
            for (int i = 0; i < 4; i++) {
                Player player = game.getPlayer(i);
                int action = (int) Math.floor(Math.random() * 6);
                switch (action) {
                    case 0:
                    case 1: {
                        List<TileOption> buyableTiles = game.getBuyableTiles(player);
                        if (buyableTiles.size() == 0) {
                            LOG.debug(player.getName() + " wants to buy a tile but has no option.");
                        } else {
                            String o = buyableTiles.stream().map(op -> op.toString()).collect(Collectors.joining(""));
                            LOG.debug(player.getName() + " wants to buy a tile and has " + buyableTiles.size() + " options : " + o + ".");
                            int r = (int) Math.floor(Math.random() * buyableTiles.size());
                            LOG.debug(player.getName() + " picks tile number " + r + ".");
                            try {
                                game.playerBuysTile(buyableTiles.get(r).getTile(), player);
                            } catch (IllegalGameAction illegalGameAction) {
                                LOG.debug("    ERROR : " + illegalGameAction.getMessage());
                            }
                        }
                        break;
                    }
                    case 2:
                    case 3:
                    case 4: {
                        int b = (int) Math.floor(Math.random() * 4);
                        TileType t = null;
                        switch (b) {
                            case 0:
                                t = TileType.factory;
                                break;
                            case 1:
                                t = TileType.houses;
                                break;
                            case 2:
                                t = TileType.store;
                                break;
                            case 3:
                                t = TileType.warehouse;
                                break;
                        }
                        List<TileOption> buildableTiles = game.getBuildablesTiles(t, player);
                        if (buildableTiles.size() == 0) {
                            LOG.debug(player.getName() + " want's to build a " + t.getTitle() + " but has no option.");
                        } else {
                            String o = buildableTiles.stream().map(op -> op.toString()).collect(Collectors.joining(""));
                            LOG.debug(player.getName() + " want's to build a " + t.getTitle() + " and has " + buildableTiles.size() + " options : " + o + ".");
                            int r = (int) Math.floor(Math.random() * buildableTiles.size());

                            LOG.debug(player.getName() + " picks tile number " + r + ".");
                            try {
                                game.playerBuildsTile(t, buildableTiles.get(r).getTile(), player);
                            } catch (IllegalGameAction illegalGameAction) {
                                LOG.debug("    ERROR : " + illegalGameAction.getMessage());
                            }
                        }
                        break;
                    }
                    case 5: {
                        List<TileOption> upgradableTiles = game.getUpgradableTiles(TileType.factory, player);
                        if (upgradableTiles.size() == 0) {
                            LOG.debug(player.getName() + " wants to upgrade his factories but has no option.");
                        } else {
                            String o = upgradableTiles.stream().map(op -> op.toString()).collect(Collectors.joining(""));
                            LOG.debug(player.getName() + " wants to upgrade his factories and has " + upgradableTiles.size() + " options : " + o + ".");
                            int r = (int) Math.floor(Math.random() * upgradableTiles.size());
                            LOG.debug(player.getName() + " picks tile number " + r + ".");
                            try {
                                game.playerUpgradesTile(TileType.factory, upgradableTiles.get(r).getTile(), player);
                            } catch (IllegalGameAction illegalGameAction) {
                                LOG.debug("    ERROR : " + illegalGameAction.getMessage());
                            }
                        }
                        break;
                    }
                    default:
                        LOG.info(player.getName() + " does nothing.");
                }
            } // all players has played

            LOG.debug("COMPUTING NEXT TURN !");
            game.info();
            game.nextTurn();

            LOG.debug("END OF TURN ! During this turn the following happended :");
            gameActionEventBus.emptyQueue();
            turnEvents.stream().forEach(e -> LOG.info("         " + e.getMessage()));
            turnEvents.clear();

            turn++;
        }
        LOG.info("Game has ended. Map is full !");
        for (int i = 0; i < 4; i++) {
            Player p = game.getPlayer(i);
            LOG.info(" > player " + p.getName() + " has " + game.getPlayerTiles(p).size() + " tiles.");
        }

        gameActionEventBus.emptyQueue();

        Thread.sleep(250);
        context.close();
    }
}
