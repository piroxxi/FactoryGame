package fr.piroxxi.factorygame.event.events;

import fr.piroxxi.factorygame.core.model.Player;
import fr.piroxxi.factorygame.event.ActionEvent;

public class ActionPlayerReceivedMoney extends ActionEvent {
    private final int amount;
    private final String reason;

    public ActionPlayerReceivedMoney(Player player, int amount, String reason) {
        super(player);
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " received " + amount + "$ (" + reason + ")";
    }
}
