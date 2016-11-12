package fr.piroxxi.factorygame.core.model;

import fr.piroxxi.factorygame.core.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
public class Game {
    private static Logger LOG = LoggerFactory.getLogger(Game.class);

    @Id
    @GeneratedValue
    private Long id;

    private int size;

    private boolean initialized = false;

    private boolean started = false;

    @OneToMany
    private Map<String, Tile> map;

    @OneToMany
    private List<Player> players;

    /* Prices are defined here */
    private static final int TILE_PRICE = 250;
    private static final double TILE_PRICE_PROGRESSION_INDICE = 0.08;
    private static final Map<TileType, Integer> BUILDING_PRICES = new HashMap<>();

    static {
        BUILDING_PRICES.put(TileType.factory, 750);
        BUILDING_PRICES.put(TileType.bigFactory, 2200);
        BUILDING_PRICES.put(TileType.houses, 950);
        BUILDING_PRICES.put(TileType.store, 800);
        BUILDING_PRICES.put(TileType.warehouse, 750);
    }

    public Game() {
    }

    /**
     * This is the normal constructor.
     *
     * @param size
     */
    public Game(int size) {
        this.size = size;
        this.players = new ArrayList<>();
    }

    public Tile getTile(int x, int y) {
        if (this.map == null) throw new UninitializedGameException("Trying to get the tile (" + x + ":" + y + ") ");
        return this.map.get(x + ":" + y);
    }

    public void generateMap() {
        this.map = new HashMap<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                this.map.put(x + ":" + y, new Tile(x, y));
            }
        }
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isStarted() {
        return started;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
        switch (this.players.size()) {
            case 1:
                this.map.get("0:0").setOwner(p);
                break;
            case 2:
                this.map.get((this.size - 1) + ":0").setOwner(p);
                break;
            case 3:
                this.map.get("0:" + (this.size - 1)).setOwner(p);
                break;
            case 4:
                this.map.get((this.size - 1) + ":" + (this.size - 1)).setOwner(p);
                break;
            default:
                throw new TooManyPlayersInGameException();
        }

    }

    public Player getPlayer(int i) {
        return this.players.get(i);
    }

    /* *********** */

    /**
     * Indicates if one of the tile's neighbors has the player p has an owner
     */
    private boolean isTileNeighborToPlayer(Tile tile, Player p) {
        Tile neighbor = this.getTile(tile.getX(), tile.getY() - 1);
        if (neighbor != null && neighbor.getOwner() == p) return true;
        neighbor = this.getTile(tile.getX() - 1, tile.getY());
        if (neighbor != null && neighbor.getOwner() == p) return true;
        neighbor = this.getTile(tile.getX(), tile.getY() + 1);
        if (neighbor != null && neighbor.getOwner() == p) return true;
        neighbor = this.getTile(tile.getX() + 1, tile.getY());
        if (neighbor != null && neighbor.getOwner() == p) return true;
        return false;
    }

    private int getTileBuyingPrice(Tile tile, Player player) {
        // FORMULA IS : = ROUND( (1+(C1)*(C1)*0.06)*240 / 10 ) * 10
        int nbTiles = getPlayerTiles(player).size();
        int tilePrice = (int) (TILE_PRICE * (1 + (nbTiles * nbTiles * TILE_PRICE_PROGRESSION_INDICE)));
        // TODO price should be modified by the tile position
        return (int) Math.round(tilePrice / 10.0) * 10;
    }

    /* *********** */

    public void playerBuysTile(Tile tile, Player player) throws IllegalGameAction {
        if (tile.getOwner() != null)
            throw new IllegalBuyingAction("The tile " + tile.toString() + " already belong's to player " + player.getName() + ".");

        if (!isTileNeighborToPlayer(tile, player))
            throw new IllegalBuyingAction("The tile " + tile.toString() + " is not adjacent to player " + player.getName() + ".");

        int price = getTileBuyingPrice(tile, player);
        if (player.getMoney() < price)
            throw new CantPayException("Player " + player.getName() + " can't afford to pay " + price + " for the tile (" + tile.getX() + ":" + tile.getY() + ") !");

        player.pay(price);
        tile.setOwner(player);
    }

    public void playerBuildsTile(TileType type, Tile tile, Player player) throws IllegalGameAction {
        if (tile.getOwner() != player)
            throw new IllegalBuildingAction("The tile " + tile.toString() + " doesn't belong to the player " + player.getName() + ".");

        if (tile.getType() != TileType.empty)
            throw new IllegalBuildingAction("The tile " + tile.toString() + " isn't empty.");

        if (player.getMoney() < BUILDING_PRICES.get(type))
            throw new CantPayException("Player " + player.getName() + " can't afford to pay " + BUILDING_PRICES.get(type) + " for : " + type.getTitle() + "!");

        player.pay(BUILDING_PRICES.get(type));
        tile.setType(type);
    }

    public void playerUpgradesTile(TileType type, Tile tile, Player player) throws IllegalGameAction {
        if (type != TileType.factory)
            throw new UnupgradableTileType("Tiles of type " + type.getTitle() + " cannot be upgraded!");

        if (tile.getOwner() != player)
            throw new IllegalBuildingAction("The tile " + tile.toString() + " doesn't belong to the player " + player.getName() + ".");

        if (tile.getType() != type)
            throw new IllegalBuildingAction("The tile " + tile.toString() + " doesn't contain the right type of building.");

        TileType t = null;
        switch (type) {
            case factory:
                t = TileType.bigFactory;
                break;
        }

        if (player.getMoney() < BUILDING_PRICES.get(t))
            throw new CantPayException("Player " + player.getName() + " can't afford to pay " + BUILDING_PRICES.get(t) + " for : " + t.getTitle() + "!");

        player.pay(BUILDING_PRICES.get(t));
        tile.setType(t);
    }

    /* *********** */

    public List<TileOption> getFreeTiles() {
        List<TileOption> tiles = this.map.values().stream()
                .filter(t -> t.getOwner() == null)
                .map(t -> new TileOption(t, null, 0))
                .collect(Collectors.toList());
        return tiles;
    }

    public List<TileOption> getPlayerTiles(Player player) {
        List<TileOption> tiles = this.map.values().stream()
                .filter(t -> t.getOwner() == player)
                .map(t -> new TileOption(t, player, 0))
                .collect(Collectors.toList());
        return tiles;
    }


    public List<TileOption> getBuyableTiles(Player player) {
        List<TileOption> tiles = this.map.values().stream()
                .filter(tile -> tile.getOwner() == null && isTileNeighborToPlayer(tile, player))
                .map(t -> new TileOption(t, player, getTileBuyingPrice(t, player)))
                .collect(Collectors.toList());
        return tiles;
    }

    public List<TileOption> getBuildablesTiles(TileType type, Player player) {
        List<TileOption> tiles = this.map.values().stream()
                .filter(t -> t.getOwner() == player && t.getType() == TileType.empty)
                .map(t -> new TileOption(t, player, BUILDING_PRICES.get(type)))
                .collect(Collectors.toList());
        return tiles;
    }

    public List<TileOption> getUpgradableTiles(TileType type, Player player) {
        if (type != TileType.factory)
            throw new UnupgradableTileType("Tiles of type " + type.getTitle() + " cannot be upgraded!");
        List<TileOption> tiles = this.map.values().stream()
                .filter(t -> t.getOwner() == player && t.getType() == type)
                .map(t -> new TileOption(t, player, BUILDING_PRICES.get(type)))
                .collect(Collectors.toList());
        return tiles;
    }

    /* *********** */

    public void nextTurn() {
        LOG.info("Calculating next turn initial variables");
        for (Player p : this.players) {
            List<TileOption> factories = getPlayerTiles(p).stream()
                    .filter(t -> t.getTile().getType() == TileType.factory)
                    .collect(Collectors.toList());
            int a = factories.size() * 160;
            List<TileOption> bigFactories = getPlayerTiles(p).stream()
                    .filter(t -> t.getTile().getType() == TileType.bigFactory)
                    .collect(Collectors.toList());
            int b = bigFactories.size() * 380;
            LOG.info("    Player " + p.getName() + " earns " + a + "$ thanks to his " + factories.size() + " factories" +
                    " and " + b + "$ thanks to his " + bigFactories.size() + " big factories.");
            p.receive(a);
            p.receive(b);
        }
    }

    /* *********** */

    public void info() {
        LOG.info("--------------------------------------");
        if (!this.isInitialized()) {
            LOG.info("Game is not yet initialized");
            return;
        }
        if (this.players.size() == 0) {
            LOG.info("There is currently no player in this game");
        } else {
            String pp = this.players.stream()
                    .map(p -> p.getName() + " (" + p.getMoney() + "$)")
                    .collect(Collectors.joining(", "));
            LOG.info("There is currently " + this.players.size() + " player(s) : " + pp);
        }
        LOG.info("Current board state :");
        for (int x = 0; x < this.size; x++) {
            String line = "";
            for (int y = 0; y < this.size; y++) {
                line += this.map.get(x + ":" + y).toString();
            }
            LOG.info(line);
        }
    }
}
