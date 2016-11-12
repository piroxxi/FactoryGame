package fr.piroxxi.factorygame.event.events;

import fr.piroxxi.factorygame.core.model.Player;
import fr.piroxxi.factorygame.core.model.Tile;
import fr.piroxxi.factorygame.core.model.TileType;
import fr.piroxxi.factorygame.event.ActionEvent;

public class ActionBuild extends ActionEvent {
    private final Tile tile;
    private final TileType type;
    private int price;

    public ActionBuild(Tile tile, Player player, TileType type, int price) {
        super(player);
        this.tile = tile;
        this.type = type;
        this.price = price;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " built a " + type.getTitle() + " on tile (" + tile.getX() + ":" + tile.getY() + ") for " + price + "$.";
    }
}
