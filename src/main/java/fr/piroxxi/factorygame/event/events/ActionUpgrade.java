package fr.piroxxi.factorygame.event.events;

import fr.piroxxi.factorygame.core.model.Player;
import fr.piroxxi.factorygame.core.model.Tile;
import fr.piroxxi.factorygame.core.model.TileType;
import fr.piroxxi.factorygame.event.ActionEvent;

public class ActionUpgrade extends ActionEvent {
    private final Tile tile;
    private final TileType oldType;
    private final TileType newType;
    private final Integer price;

    public ActionUpgrade(Tile tile, Player player, TileType oldType, TileType newType, Integer price) {
        super(player);
        this.tile = tile;
        this.oldType = oldType;
        this.newType = newType;
        this.price = price;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " upgrade a " + oldType.getTitle() + " to a " + newType.getTitle() + " on tile (" + tile.getX() + ":" + tile.getY() + ") for " + price + "$.";
    }
}
