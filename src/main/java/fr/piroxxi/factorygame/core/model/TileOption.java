package fr.piroxxi.factorygame.core.model;

/**
 * One of the available options for a player's action.
 */
public class TileOption {
    private Tile tile;
    private Player player;
    private int amount;

    public TileOption(Tile tile, Player player, int amount) {
        this.tile = tile;
        this.player = player;
        this.amount = amount;
    }

    public Tile getTile() {
        return tile;
    }

    public int getAmount() {
        return amount;
    }

    public Player getPlayer() {
        return player;
    }

    public String toString() {
        return "[" + tile.getX() + ":" + tile.getY() + " -> " + amount + "$]";
    }
}
