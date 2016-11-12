package fr.piroxxi.factorygame.event;

import fr.piroxxi.factorygame.core.model.Player;

public abstract class ActionEvent {
    protected Player player;

    public ActionEvent(Player player) {
        this.player = player;
    }

    public String getMessage() {
        return "Player " + player.getName();
    }
}
