package fr.piroxxi.factorygame.event.events;

import fr.piroxxi.factorygame.core.model.Player;
import fr.piroxxi.factorygame.core.model.Tile;
import fr.piroxxi.factorygame.event.ActionEvent;

public class ActionBuyTile extends ActionEvent {
    private final Tile tile;
    private int price;

    public ActionBuyTile(Tile tile, Player player, int price) {
        super(player);
        this.tile = tile;
        this.price = price;
    }

    public String getMessage() {
        return super.getMessage() + " bought tile (" + tile.getX() + ":" + tile.getY() + ") for " + price + "$.";
    }
}
