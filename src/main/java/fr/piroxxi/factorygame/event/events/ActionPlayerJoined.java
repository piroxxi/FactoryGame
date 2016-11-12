package fr.piroxxi.factorygame.event.events;

import fr.piroxxi.factorygame.core.model.Player;
import fr.piroxxi.factorygame.event.ActionEvent;

/**
 * Created by PiroXXI on 12/11/2016.
 */
public class ActionPlayerJoined extends ActionEvent {
    public ActionPlayerJoined(Player player) {
        super(player);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " joined the game.";
    }
}
